package ru.t1.java.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "cache")
public class CacheConfig {// класс для работы с временем жизни кэша

    private long ttl; //time to live

    public Duration getTtl(){
        return Duration.ofSeconds(ttl);// преобразуем время в Duration секунды, toMillis() переведет в секунды
    }


    public void setTtl(long ttl){
        this.ttl = ttl;// здесь устанавливаем время из yml
    }

}
