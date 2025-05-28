package ru.t1.java.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDto {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
}
