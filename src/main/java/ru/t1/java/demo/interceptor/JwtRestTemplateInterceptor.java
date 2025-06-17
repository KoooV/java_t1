package ru.t1.java.demo.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Value("${security.jwt.token}")
    private String jwtToken;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().add("Authorization", "Bearer " + jwtToken);
        return execution.execute(request, body);
    }
} 