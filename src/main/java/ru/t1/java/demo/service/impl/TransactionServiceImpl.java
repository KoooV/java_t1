package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.aspect.annotation.DataSourceError;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public Transaction getAmountById(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getAllTransaction() {
        return transactionRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction transaction = convertToEntity(transactionDto);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionDto updateTransaction(TransactionDto transactionDto, Long id) {
        Transaction existingTransaction = transactionRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
        
        existingTransaction.setAmount(transactionDto.getAmount());
        existingTransaction.setTimestamp(transactionDto.getTransactionDate());
        
        Account account = accountRepository.findById(transactionDto.getAccountId())
            .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + transactionDto.getAccountId()));
        existingTransaction.setAccount(account);
        
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return convertToDto(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new NoSuchElementException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    private TransactionDto convertToDto(Transaction transaction) {
        return TransactionDto.builder()
            .id(transaction.getId())
            .accountId(transaction.getAccount().getId())
            .amount(transaction.getAmount())
            .transactionDate(transaction.getTimestamp())
            .build();
    }

    private Transaction convertToEntity(TransactionDto dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(dto.getTransactionDate());
        
        Account account = accountRepository.findById(dto.getAccountId())
            .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + dto.getAccountId()));
        
        transaction.setAccount(account);
        
        return transaction;
    }

    @DataSourceError(operationType = "CREATE", entityType = "TRANSACTION")
    public Transaction create(Transaction transaction) {
        // ... код метода
        return transactionRepository.save(transaction);
    }
    
    @DataSourceError(operationType = "UPDATE", entityType = "TRANSACTION")
    public Transaction update(Transaction transaction) {
        // ... код метода
        return transactionRepository.save(transaction);
    }
}
