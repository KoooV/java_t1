package ru.t1.java.demo.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction extends AbstractPersistable<Long> {

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amount_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(name = "time", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();//время транзакции

    public Transaction(BigDecimal amount, Account account, LocalDateTime timestamp) {
        this.amount = amount;
        this.account = account;
        this.timestamp = timestamp;
    }

    public Transaction() {
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Account getAccount() {
        return this.account;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class TransactionBuilder {
        private BigDecimal amount;
        private Account account;
        private LocalDateTime timestamp;

        TransactionBuilder() {
        }

        public TransactionBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder account(Account account) {
            this.account = account;
            return this;
        }

        public TransactionBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Transaction build() {
            return new Transaction(this.amount, this.account, this.timestamp);
        }

        public String toString() {
            return "Transaction.TransactionBuilder(amount=" + this.amount + ", account=" + this.account + ", timestamp=" + this.timestamp + ")";
        }
    }
}
