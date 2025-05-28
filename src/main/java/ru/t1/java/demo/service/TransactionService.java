package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;


import java.util.List;

public interface TransactionService {
    Transaction getAmountById(Long id);
    List<TransactionDto> getAllTransaction();
    TransactionDto createTransaction(TransactionDto transactionDto);
    TransactionDto updateTransaction(TransactionDto transactionDto, Long id);
    void deleteTransaction(Long id);
    List<TransactionDto> getTransactionsByAccountId(Long accountId);






}