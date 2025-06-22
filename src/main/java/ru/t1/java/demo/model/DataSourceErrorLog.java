package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;
import io.hypersistence.utils.hibernate.type.json.JsonType;

@Entity
@Table(name = "data_source_error_logs")
@Getter
@Setter
@NoArgsConstructor
public class DataSourceErrorLog extends AbstractPersistable<Long> {

    @Column(name = "message", nullable = false)
    private String errorMessage;

    @Column(name = "stack_trace", columnDefinition = "TEXT")// цепочка вызовов методов, которая привела к ошибке
    private String stackTrace;

    @Type(JsonType.class)
    @Column(name = "method_signature", columnDefinition = "jsonb")// хранение сигнатуры метода в виде json
    private String methodSignature;
}
