package ru.t1.java.demo.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void getAllTransaction_shouldReturnTransactionDtoList() {
        // given
        Account account = new Account();
        ReflectionTestUtils.setField(account, "id", 1L);

        Transaction transaction = new Transaction();
        ReflectionTestUtils.setField(transaction, "id", 1L);
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.TEN);
        transaction.setTimestamp(LocalDateTime.now());

        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        // when
        List<TransactionDto> result = transactionService.getAllTransaction();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction.getId(), result.get(0).getId());
        verify(transactionRepository).findAll();
    }

    @Test
    void createTransaction_shouldSaveAndReturnDto() {
        // given
        Account account = new Account();
        ReflectionTestUtils.setField(account, "id", 1L);

        TransactionDto dto = TransactionDto.builder()
                .accountId(1L)
                .amount(BigDecimal.ONE)
                .transactionDate(LocalDateTime.now())
                .build();

        Transaction transaction = new Transaction();
        transaction.setAccount(account);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // when
        TransactionDto result = transactionService.createTransaction(dto);

        // then
        assertNotNull(result);
        verify(accountRepository).findById(1L);
        verify(transactionRepository).save(any(Transaction.class));
    }
} 