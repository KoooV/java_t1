package ru.t1.java.demo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "t1.metrics")// связывает с metric в application yml
@Data
public class MetricConfig {
    private long timeLimit;// предел работы метода
    private boolean enabled;// логирование вкл/выкл
    private List<String> includePackages;// пакеты обрабатываемые аспектом
    private List<String> excludeMethods;// игнорируемые методы
} 