package ru.t1.java.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.annotation.DataSourceError;
import ru.t1.java.demo.annotation.Metric;
import ru.t1.java.demo.dto.TransactionMessage;
import ru.t1.java.demo.dto.TransactionResultMessage;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProcessingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TRANSACTION_TOPIC = "t1_demo_transactions";
    private static final String TRANSACTION_ACCEPT_TOPIC = "t1_demo_transaction_accept";
    private static final String TRANSACTION_RESULT_TOPIC = "t1_demo_transaction_result";

    /**
     * Обрабатывает новую транзакцию из топика t1_demo_transactions.
     * Создает транзакцию в БД и отправляет сообщение в топик t1_demo_transaction_accept.
     *
     * @param message JSON-сообщение с данными транзакции
     */
    @KafkaListener(topics = TRANSACTION_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    @Metric
    @DataSourceError
    public void processTransaction(String message) {
        try {
            TransactionMessage transactionMessage = objectMapper.readValue(message, TransactionMessage.class);
            log.info("Received transaction message: {}", transactionMessage);

            Account account = accountRepository.findByAccountId(transactionMessage.getAccountId());
            if (account == null) {
                log.error("Account not found: {}", transactionMessage.getAccountId());
                return;
            }

            if (account.getStatus() != Account.Status.OPEN) {
                log.warn("Account {} is not OPEN, current status: {}", account.getAccountId(), account.getStatus());
                return;
            }

            // Создаем и сохраняем транзакцию
            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactionMessage.getTransactionId());
            transaction.setAmount(transactionMessage.getAmount());
            transaction.setAccount(account);
            transaction.setStatus(Transaction.Status.REQUESTED);
            transaction.setTimestamp(LocalDateTime.now());
            transactionRepository.save(transaction);

            // Обновляем баланс счета
            account.setBalance(account.getBalance().add(transactionMessage.getAmount()));
            accountRepository.save(account);

            // Отправляем сообщение о принятии транзакции
            TransactionMessage acceptMessage = new TransactionMessage(
                    account.getClient().getClientId(),
                    account.getAccountId(),
                    transaction.getTransactionId(),
                    transaction.getTimestamp(),
                    transaction.getAmount(),
                    account.getBalance()
            );

            String acceptMessageJson = objectMapper.writeValueAsString(acceptMessage);
            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(acceptMessageJson)
                    .setHeader(KafkaHeaders.TOPIC, TRANSACTION_ACCEPT_TOPIC)
                    .build();

            kafkaTemplate.send(kafkaMessage);
            log.info("Transaction accepted and message sent to {}: {}", TRANSACTION_ACCEPT_TOPIC, acceptMessage);

        } catch (Exception e) {
            log.error("Error processing transaction message: {}", message, e);
            throw new RuntimeException("Failed to process transaction", e);
        }
    }

    /**
     * Обрабатывает результат валидации транзакции из топика t1_demo_transaction_result.
     * В зависимости от статуса:
     * - ACCEPTED: обновляет статус транзакции
     * - BLOCKED: блокирует транзакцию и счет, замораживает средства
     * - REJECTED: отклоняет транзакцию и возвращает средства
     *
     * @param message JSON-сообщение с результатом валидации
     */
    @KafkaListener(topics = TRANSACTION_RESULT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    @Metric
    @DataSourceError
    public void processTransactionResult(String message) {
        try {
            TransactionResultMessage resultMessage = objectMapper.readValue(message, TransactionResultMessage.class);
            log.info("Received transaction result: {}", resultMessage);

            Transaction transaction = transactionRepository.findByTransactionId(resultMessage.getTransactionId());
            if (transaction == null) {
                log.error("Transaction not found: {}", resultMessage.getTransactionId());
                return;
            }

            Account account = transaction.getAccount();
            if (account == null) {
                log.error("Account not found for transaction: {}", resultMessage.getTransactionId());
                return;
            }

            switch (resultMessage.getStatus()) {
                case ACCEPTED:
                    transaction.setStatus(Transaction.Status.ACCEPTED);
                    transactionRepository.save(transaction);
                    log.info("Transaction {} accepted", transaction.getTransactionId());
                    break;

                case BLOCKED:
                    transaction.setStatus(Transaction.Status.BLOCKED);
                    transactionRepository.save(transaction);
                    
                    // Обновляем статус счета и замораживаем средства
                    account.setStatus(Account.Status.BLOCKED);
                    BigDecimal frozenAmount = account.getFrozenAmount() != null ? 
                            account.getFrozenAmount().add(transaction.getAmount()) : 
                            transaction.getAmount();
                    account.setFrozenAmount(frozenAmount);
                    accountRepository.save(account);
                    log.info("Transaction {} blocked, account {} blocked with frozen amount {}", 
                            transaction.getTransactionId(), account.getAccountId(), frozenAmount);
                    break;

                case REJECTED:
                    transaction.setStatus(Transaction.Status.REJECTED);
                    transactionRepository.save(transaction);
                    
                    // Возвращаем средства на счет
                    account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                    accountRepository.save(account);
                    log.info("Transaction {} rejected, balance adjusted for account {}", 
                            transaction.getTransactionId(), account.getAccountId());
                    break;

                default:
                    log.warn("Unknown transaction status: {}", resultMessage.getStatus());
            }

        } catch (Exception e) {
            log.error("Error processing transaction result: {}", message, e);
            throw new RuntimeException("Failed to process transaction result", e);
        }
    }
} 