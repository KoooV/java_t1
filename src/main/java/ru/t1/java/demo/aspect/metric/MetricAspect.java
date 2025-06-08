package ru.t1.java.demo.aspect.metric;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.config.MetricConfig;
import ru.t1.java.demo.model.TimeLimitExceedLog;
import ru.t1.java.demo.repository.TimeLimitExceedLogRepository;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    private final Logger log = LoggerFactory.getLogger(MetricAspect.class);
    private final ObjectMapper objectMapper;
    private final TimeLimitExceedLogRepository repository;
    private final MetricConfig config;

    @Around("@annotation(ru.t1.java.demo.aspect.annotation.Metric)")
    public Object measuringTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long totalTime = System.currentTimeMillis() - startTime;//время отработки метода в миллисекундах

        if(totalTime > config.getTimeLimit()){
            saveProceed(joinPoint);
        }
        return result;//в Around обязательно вернуть результат или метод зависнет
    }

    public void saveProceed(ProceedingJoinPoint joinPoint){
        TimeLimitExceedLog errorLog = new TimeLimitExceedLog();
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Map<String, Object> methodInfo = new HashMap();
            methodInfo.put("className", signature.getDeclaringTypeName());//название класса
            methodInfo.put("methodName", signature.getName());// название метода
            methodInfo.put("parameterName", signature.getParameterNames());// название параметров метода
            methodInfo.put("parameterTypes", signature.getParameterTypes());// типы параметров
            errorLog.setMethodSignature(objectMapper.writeValueAsString(methodInfo));// конвертация в json
            repository.save(errorLog);
        }catch(Exception ex){
            log.error("Couldn't convert to JSON {}", ex.getMessage());

        }
    }
//    private Object methodType(MethodSignature signature){
//        String methodName = signature.getMethod().getName();//название метода
//        return methodName;
//
//    }
//
//    private Object entityType(MethodSignature signature){
//        String entityName = signature.getDeclaringType().getName();//название класса
//        return entityName
//                .replace("Controller", "");
//    }

}
