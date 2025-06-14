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
import ru.t1.java.demo.config.TransactionConfig;
import ru.t1.java.demo.dto.TransactionMessage;
import ru.t1.java.demo.dto.TransactionResultMessage;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionValidationService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final TransactionConfig transactionConfig;

    private static final String TRANSACTION_ACCEPT_TOPIC = "t1_demo_transaction_accept";
    private static final String TRANSACTION_RESULT_TOPIC = "t1_demo_transaction_result";

    /**
     * Валидирует транзакцию из топика t1_demo_transaction_accept.
     * Выполняет следующие проверки:
     * 1. Проверка количества транзакций за период времени
     * 2. Проверка достаточности средств на счете
     * Отправляет результат валидации в топик t1_demo_transaction_result.
     *
     * @param message JSON-сообщение с данными транзакции
     */
    @KafkaListener(topics = TRANSACTION_ACCEPT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void validateTransaction(String message) {
        try {
            TransactionMessage transactionMessage = objectMapper.readValue(message, TransactionMessage.class);
            log.info("Received transaction for validation: {}", transactionMessage);

            Account account = accountRepository.findByAccountId(transactionMessage.getAccountId());
            if (account == null) {
                log.error("Account not found: {}", transactionMessage.getAccountId());
                return;
            }

            // Проверка количества транзакций за период
            LocalDateTime startTime = LocalDateTime.now().minusMinutes(transactionConfig.getTimeWindowMinutes());
            List<Transaction> recentTransactions = transactionRepository.findRecentTransactionsByAccount(
                    account.getAccountId(), startTime);

            if (recentTransactions.size() >= transactionConfig.getMaxTransactionsPerPeriod()) {
                // Блокируем N транзакций
                recentTransactions.stream()
                        .limit(transactionConfig.getMaxTransactionsPerPeriod())
                        .forEach(transaction -> {
                            transaction.setStatus(Transaction.Status.BLOCKED);
                            transactionRepository.save(transaction);
                            sendTransactionResult(transaction.getAccount().getAccountId(),
                                    transaction.getTransactionId(), Transaction.Status.BLOCKED);
                        });
                return;
            }

            // Проверка баланса
            if (transactionMessage.getAmount().compareTo(account.getBalance()) > 0) {
                Transaction transaction = transactionRepository.findByTransactionId(transactionMessage.getTransactionId());
                if (transaction != null) {
                    transaction.setStatus(Transaction.Status.REJECTED);
                    transactionRepository.save(transaction);
                    sendTransactionResult(transaction.getAccount().getAccountId(),
                            transaction.getTransactionId(), Transaction.Status.REJECTED);
                }
                return;
            }

            // Если все проверки пройдены
            Transaction transaction = transactionRepository.findByTransactionId(transactionMessage.getTransactionId());
            if (transaction != null) {
                transaction.setStatus(Transaction.Status.ACCEPTED);
                transactionRepository.save(transaction);
                sendTransactionResult(transaction.getAccount().getAccountId(),
                        transaction.getTransactionId(), Transaction.Status.ACCEPTED);
            }

        } catch (Exception e) {
            log.error("Error validating transaction: {}", message, e);
            throw new RuntimeException("Failed to validate transaction", e);
        }
    }

    /**
     * Отправляет результат валидации транзакции в топик t1_demo_transaction_result.
     *
     * @param accountId ID счета
     * @param transactionId ID транзакции
     * @param status Статус транзакции (ACCEPTED, REJECTED, BLOCKED)
     */
    private void sendTransactionResult(UUID accountId, UUID transactionId, Transaction.Status status) {
        try {
            TransactionResultMessage resultMessage = new TransactionResultMessage(accountId, transactionId, status);
            String resultJson = objectMapper.writeValueAsString(resultMessage);
            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(resultJson)
                    .setHeader(KafkaHeaders.TOPIC, TRANSACTION_RESULT_TOPIC)
                    .build();

            kafkaTemplate.send(kafkaMessage);
            log.info("Transaction result sent: {}", resultMessage);
        } catch (Exception e) {
            log.error("Error sending transaction result", e);
        }
    }
} 