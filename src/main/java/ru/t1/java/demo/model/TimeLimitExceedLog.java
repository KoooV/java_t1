package ru.t1.java.demo.model;


import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "time_limit_exceed_log")
public class TimeLimitExceedLog extends AbstractPersistable<Long> {

    @Type(JsonType.class)
    @Column(name = "error", columnDefinition = "jsonb")
    private String error;
}
