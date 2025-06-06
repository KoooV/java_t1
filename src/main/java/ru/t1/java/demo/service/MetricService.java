package ru.t1.java.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MetricService {
    
    public void recordMethodExecutionTime(String methodSignature, long executionTime) {

        log.debug("Method {} executed in {}ms", methodSignature, executionTime);
    }
} 