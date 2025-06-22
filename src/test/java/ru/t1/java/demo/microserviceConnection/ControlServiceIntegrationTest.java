package ru.t1.java.demo.microserviceConnection;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import ru.t1.java.demo.dto.AccountStatusDto;
import ru.t1.java.demo.dto.ClientStatusDto;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "unblock.service.url=http://localhost:${wiremock.server.port}",
        "metrics.name.blockedClients=10",
        "metrics.name.arrestedAccounts=10"
})
class ControlServiceIntegrationTest {

    @Autowired
    private ControlService controlService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        WireMock.reset();
    }

    @Test
    void unblockClient_shouldSendRequestToUnblockService() {
        // given
        UUID clientId1 = UUID.randomUUID();
        UUID clientId2 = UUID.randomUUID();
        ClientStatusDto clientStatusDto1 = new ClientStatusDto(Client.Status.BLOCKED, clientId1);
        ClientStatusDto clientStatusDto2 = new ClientStatusDto(Client.Status.BLOCKED, clientId2);
        when(clientRepository.clientsToUnlock(PageRequest.of(0, 10))).thenReturn(List.of(clientStatusDto1, clientStatusDto2));

        stubFor(post(urlPathEqualTo("/unblock-client"))
                .withQueryParam("clientId", equalTo(clientId1.toString()))
                .willReturn(aResponse().withStatus(200)));

        stubFor(post(urlPathEqualTo("/unblock-client"))
                .withQueryParam("clientId", equalTo(clientId2.toString()))
                .willReturn(aResponse().withStatus(200)));

        // when
        controlService.unblockClient();

        // then
        verify(postRequestedFor(urlPathEqualTo("/unblock-client"))
                .withQueryParam("clientId", equalTo(clientId1.toString())));
        verify(postRequestedFor(urlPathEqualTo("/unblock-client"))
                .withQueryParam("clientId", equalTo(clientId2.toString())));
    }

    @Test
    void unarrestAccount_shouldSendRequestToUnarrestService() {
        // given
        UUID accountId1 = UUID.randomUUID();
        UUID accountId2 = UUID.randomUUID();
        AccountStatusDto accountStatusDto1 = new AccountStatusDto(accountId1, null);
        AccountStatusDto accountStatusDto2 = new AccountStatusDto(accountId2, null);
        when(accountRepository.accountToUnarrested(PageRequest.of(0, 10))).thenReturn(List.of(accountStatusDto1, accountStatusDto2));

        stubFor(post(urlPathEqualTo("/unblock-account"))
                .withQueryParam("accountId", equalTo(accountId1.toString()))
                .willReturn(aResponse().withStatus(200)));

        stubFor(post(urlPathEqualTo("/unblock-account"))
                .withQueryParam("accountId", equalTo(accountId2.toString()))
                .willReturn(aResponse().withStatus(200)));

        // when
        controlService.unarrestAccount();

        // then
        verify(postRequestedFor(urlPathEqualTo("/unblock-account"))
                .withQueryParam("accountId", equalTo(accountId1.toString())));
        verify(postRequestedFor(urlPathEqualTo("/unblock-account"))
                .withQueryParam("accountId", equalTo(accountId2.toString())));
    }
} 