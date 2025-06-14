package ru.t1.java.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.Transaction;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResultMessage {
    private UUID accountId;
    private UUID transactionId;
    private Transaction.Status status;
} 