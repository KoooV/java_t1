package ru.t1.java.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "data_source_error_log")
public class DataSourceErrorLog extends AbstractPersistable<Long> {

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "method_signature")
    private String methodSignature;


}
