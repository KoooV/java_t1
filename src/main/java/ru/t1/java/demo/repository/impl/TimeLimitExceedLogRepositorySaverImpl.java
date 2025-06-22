package ru.t1.java.demo.repository.impl;

import org.example.aspectspringbootstarter.pojo.TimeLimitExceedLogPojo;
import org.example.aspectspringbootstarter.repository.TimeLimitExceedLogRepositorySaver;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.TimeLimitExceedLog;
import ru.t1.java.demo.repository.TimeLimitExceedLogRepository;

@Component
public class TimeLimitExceedLogRepositorySaverImpl implements TimeLimitExceedLogRepositorySaver {//реализация репозитория из стартера
    private final TimeLimitExceedLogRepository jpaRepository;

    public TimeLimitExceedLogRepositorySaverImpl(TimeLimitExceedLogRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void saveLimit(TimeLimitExceedLogPojo errorLog){
        TimeLimitExceedLog entity = new TimeLimitExceedLog();
        entity.setError(errorLog.getError());
        jpaRepository.save(entity);
    }
}
