package ru.t1.java.demo.repository;

import org.example.aspectspringbootstarter.pojo.TimeLimitExceedLogPojo;
import org.example.aspectspringbootstarter.repository.TimeLimitExceedLogRepositorySaver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.TimeLimitExceedLog;

@Repository
public interface TimeLimitExceedLogRepository extends JpaRepository<TimeLimitExceedLog, Long> {

}