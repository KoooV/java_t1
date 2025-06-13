package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractPersistable<Long> {

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_id")
    private UUID transactionId = UUID.randomUUID();//сквозной id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amount_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(name = "time", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();//время транзакции

    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false)//статус транзакции
    private Status status;

    public enum Status{
        ACCEPTED,
        REJECTED,
        BLOCKED,
        CANCELLED,
        REQUESTED
    }
}
