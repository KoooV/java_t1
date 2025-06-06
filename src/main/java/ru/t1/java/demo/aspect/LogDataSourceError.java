package ru.t1.java.demo.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
@RequiredArgsConstructor
public class LogDataSourceError {
    private static Logger log = LoggerFactory.getLogger(LogDataSourceError.class);
    private final DataSourceErrorLogRepository repository;
    private final ObjectMapper objectMapper;// для преобразования в json

    @Around("@annotation(DataSourceError)")
    public Object logError(ProceedingJoinPoint joinPoint, LogDataSourceError logDataSourceError
    ) throws Throwable {// параметр, который предоставляет информацию - вызываемый метод, аргументы и позволяет выполнить метод через joinPoint.proceed()
        try{
            return joinPoint.proceed();
        }catch(Exception e){
            saveException(joinPoint, e, logDataSourceError);
            throw e;
        }
    }
    public void saveException(ProceedingJoinPoint joinPoint, Exception e, LogDataSourceError logDataSourceError ){
        DataSourceErrorLog errorLog = new DataSourceErrorLog();

        try{
        errorLog.setErrorMessage(e.getMessage());//сообщение об ошибке message в бд

        StringWriter source = new StringWriter();// экземпляр для получения стэка в виде строки
        e.printStackTrace(new PrintWriter(source));
        errorLog.setStackTrace(source.toString());// сохраняем накопленный стек в stack_trace бд

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();// формируем json с информацией о методе
        Map<String, Object> methodInfo = new HashMap<>();
            methodInfo.put("className", signature.getDeclaringTypeName());
            methodInfo.put("methodName", signature.getName());
            methodInfo.put("parameterTypes", signature.getParameterTypes());
            methodInfo.put("parameterNames", signature.getParameterNames());
            methodInfo.put("parameterValues", joinPoint.getArgs());
            methodInfo.put("entityType", logDataSourceError.entityType(signature));
            errorLog.setMethodSignature(objectMapper.writeValueAsString(methodInfo));// to json

        repository.save(errorLog);

        log.error("Ошибка в методе {}.{}: {}",
                signature.getDeclaringTypeName(),//имя класса
                signature.getName(),//имя метода
                e.getMessage());//текст ошибки


        }catch(Exception ex){
            log.error("Ошибка при сохранении лога: {}", ex.getMessage());

        }

    }

    private Object entityType(MethodSignature signature) {
        String entityName = signature.getDeclaringType().getSimpleName();
        return entityName
                .replace("Repository", "")
                .replace("Controller", "");
    }

}