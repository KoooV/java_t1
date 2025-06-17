package ru.t1.java.demo.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.t1.java.demo.dto.ClientStatusRequest;
import ru.t1.java.demo.dto.ClientStatusResponse;
import ru.t1.java.demo.dto.ResponseMessage;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationServiceClient {

    private final RestTemplate restTemplate;

    @Value("${validation.service.url}")
    private String validationServiceUrl;

    public ClientStatusResponse checkClientStatus(UUID clientId, UUID accountId) {
        try {
            ClientStatusRequest request = new ClientStatusRequest(clientId, accountId);
            return restTemplate.postForObject(
                validationServiceUrl + "/api/v1/client/status",
                request,
                ClientStatusResponse.class
            );
        } catch (Exception e) {
            log.error("Error checking client status for clientId: {}, accountId: {}", clientId, accountId, e);
            return new ClientStatusResponse("UNKNOWN", "Error checking client status");
        }
    }

    public ResponseMessage checkBlacklistStatus(UUID accountId) {
        try {
            return restTemplate.getForObject(
                validationServiceUrl + "/api/blacklist/check/" + accountId,
                ResponseMessage.class
            );
        } catch (Exception e) {
            log.error("Error checking blacklist status for accountId: {}", accountId, e);
            return new ResponseMessage("Error checking blacklist status", ResponseMessage.BlackListStatus.UNKNOWN);
        }
    }
} 