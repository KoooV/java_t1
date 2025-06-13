package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account extends AbstractPersistable<Long> {

    public enum Type {
        CREDIT,
        DEBIT
    }

    public enum Status{
        ARRESTED,
        BLOCKED,
        CLOSED,
        OPEN
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

    @Column(name = "account_id", nullable = false)
    private UUID accountId = UUID.randomUUID();


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "frozen_amount", nullable = false)
    private BigDecimal frozenAmount;

}
