package ru.t1.java.demo.repository.impl;

import org.example.aspectspringbootstarter.pojo.DataSourceErrorLogPojo;
import org.example.aspectspringbootstarter.repository.DataSourceErrorLogRepositorySaver;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

public class DataSourceErrorLogRepositorySaverImpl implements DataSourceErrorLogRepositorySaver {
    private final DataSourceErrorLogRepository jpaRepository;


    public DataSourceErrorLogRepositorySaverImpl(DataSourceErrorLogRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    @Override
    public void saveError(DataSourceErrorLogPojo errorLog){
        DataSourceErrorLog entity = new DataSourceErrorLog();
        entity.setErrorMessage(errorLog.getErrorMessage());
        entity.setStackTrace(errorLog.getStackTrace());
        entity.setMethodSignature(errorLog.getMethodSignature());
        jpaRepository.save(entity);
    }
}
