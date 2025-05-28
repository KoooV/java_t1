package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;

@Entity
@Table(name = "data_source_error_logs")
@Getter
@Setter
@NoArgsConstructor
public class DataSourceErrorLog extends AbstractPersistable<Long> {

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @Column(name = "error_class", nullable = false)
    private String errorClass;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "operation_type")
    private String operationType;

    @Column(name = "entity_type")
    private String entityType;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
