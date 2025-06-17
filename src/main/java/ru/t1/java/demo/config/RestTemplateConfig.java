package ru.t1.java.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.t1.java.demo.interceptor.JwtRestTemplateInterceptor;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(JwtRestTemplateInterceptor jwtInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(jwtInterceptor);
        return restTemplate;
    }
} 