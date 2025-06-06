package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "time_limit_exceed_logs")
@Getter
@Setter
@NoArgsConstructor
public class TimeLimitExceedLog extends AbstractPersistable<Long> {

    @Column(name = "method_name", nullable = false)
    private String methodName;

    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Column(name = "execution_time", nullable = false)
    private long executionTime;

    @Column(name = "time_limit", nullable = false)
    private long timeLimit;

    @Column(name = "method_signature", columnDefinition = "jsonb")
    private String methodSignature;

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
