package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "accounts")
public class Account extends AbstractPersistable<Long> {

    public Account(Client client, BigDecimal balance, Type type, List<Transaction> transaction) {
        this.client = client;
        this.balance = balance;
        this.type = type;
        this.transaction = transaction;
    }

    public Account() {
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public Client getClient() {
        return this.client;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Type getType() {
        return this.type;
    }

    public List<Transaction> getTransaction() {
        return this.transaction;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }

    public enum Type {
        CREDIT,
        DEBIT
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;


    @Column(name = "balance")// по дэфолту percision = 19, scale = 2
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transaction;

    public static class AccountBuilder {
        private Client client;
        private BigDecimal balance;
        private Type type;
        private List<Transaction> transaction;

        AccountBuilder() {
        }

        public AccountBuilder client(Client client) {
            this.client = client;
            return this;
        }

        public AccountBuilder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public AccountBuilder transaction(List<Transaction> transaction) {
            this.transaction = transaction;
            return this;
        }

        public Account build() {
            return new Account(this.client, this.balance, this.type, this.transaction);
        }

        public String toString() {
            return "Account.AccountBuilder(client=" + this.client + ", balance=" + this.balance + ", type=" + this.type + ", transaction=" + this.transaction + ")";
        }
    }
}
