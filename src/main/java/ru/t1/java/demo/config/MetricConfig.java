package ru.t1.java.demo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.aspectspringbootstarter.interfaceToMainProject.MetricConfigStarter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties(prefix = "t1.metrics")// связывает с metric в application yml
@Data
public class MetricConfig implements MetricConfigStarter {
    private long timeLimit;// предел работы метода
    private boolean enabled;// логирование вкл/выкл
    private List<String> includePackages;// пакеты обрабатываемые аспектом
    private List<String> excludeMethods;// игнорируемые методы

    @Override
    public long getTimeLimit(){
        return timeLimit;
    }
} 