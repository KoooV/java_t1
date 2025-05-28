package ru.t1.java.demo.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.annotation.LogDataSourceError;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

import java.io.PrintWriter;
import java.io.StringWriter;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DataSourceErrorLoggingAspect {

    private final DataSourceErrorLogRepository errorLogRepository;

    @Around("@annotation(logDataSourceError)")
    public Object logDataSourceError(ProceedingJoinPoint joinPoint, LogDataSourceError logDataSourceError) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logError(joinPoint, logDataSourceError, e);
            throw e;
        }
    }

    private void logError(ProceedingJoinPoint joinPoint, LogDataSourceError annotation, Exception e) {
        try {
            DataSourceErrorLog errorLog = new DataSourceErrorLog();
            errorLog.setErrorMessage(e.getMessage());
            errorLog.setErrorClass(e.getClass().getName());
            errorLog.setStackTrace(getStackTraceAsString(e));
            errorLog.setOperationType(annotation.operationType());
            errorLog.setEntityType(annotation.entityType());

            errorLogRepository.save(errorLog);
            log.error("Database error logged: {}", e.getMessage(), e);
        } catch (Exception loggingException) {
            log.error("Failed to log database error: {}", loggingException.getMessage(), loggingException);
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
} 