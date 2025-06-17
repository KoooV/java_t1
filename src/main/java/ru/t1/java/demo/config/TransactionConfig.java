package ru.t1.java.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "transaction")
public class TransactionConfig {
    private int maxTransactionsPerPeriod = 5; // N транзакций
    private int timeWindowMinutes = 5; // T минут
    private int rejectedLimit = 3; // Лимит отклоненных транзакций
}