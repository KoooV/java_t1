package ru.t1.java.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.t1.java.demo.config.CacheConfig;
import ru.t1.java.demo.config.MetricConfig;

@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
@EnableConfigurationProperties({MetricConfig.class, CacheConfig.class})

@Slf4j
public class T1JavaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(T1JavaDemoApplication.class, args);
    }

}
