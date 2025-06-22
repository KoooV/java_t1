package ru.t1.java.demo.microserviceConnection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.t1.java.demo.dto.AccountStatusDto;
import ru.t1.java.demo.dto.ClientStatusDto;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ControlService {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;

    @Value("${unblock.service.url}")
    private String unblockServiceUrl;

    @Value("${metrics.name.blockedClients}")
    private int blockedClients;//число клиентов для запроса на разблокировку

    @Value("${metrics.name.arrestedAccounts}")
    private int arrestedAccounts;

    @Scheduled(fixedRateString = "${metrics.name.period:120000}")
    public void scheduledUnblockClients() {
        unblockClient();
    }

    @Scheduled(fixedRateString = "${metrics.name.period:120000}")
    public void scheduledUnarrestAccounts() {
        unarrestAccount();
    }

    public List<AccountStatusDto> getAccountsToUnarrested(){
        return accountRepository.accountToUnarrested(PageRequest.of(0,arrestedAccounts));
    }

    public List<ClientStatusDto> getClientsToUnblock(){
        return clientRepository.clientsToUnlock(PageRequest.of(0,blockedClients));//номер страницы + количество клиентов
    }
    public void unblockClient(){
        try{
            List<ClientStatusDto> clients = getClientsToUnblock();

            for(ClientStatusDto client : clients){
                String requestUrl = unblockServiceUrl + "/unblock-client?clientId=" + client.getClientId();
                String responce = restTemplate.postForObject(requestUrl, null, String.class);//ссылка на размещение + null тк body запроса null + тип возвращаемого объекта
                log.info("Send client to unblock with id: {}", client.getClientId());
            }
        }
        catch(Exception e){
            log.error("Error sending client to unblock microservice", e);
        }
    }

    public void unarrestAccount(){
        try{
        List<AccountStatusDto> accounts = getAccountsToUnarrested();
        for(AccountStatusDto account : accounts ){
            String requestUrl = unblockServiceUrl + "/unblock-account?accountId=" + account.getAccountId();
            String responce = restTemplate.postForObject(requestUrl, null, String.class);
            log.info("Send account to unarrested with id: {}", account.getAccountId());
        }
        }catch(Exception c){
            log.error("Error sending account to unarrested microservice", c);
        }



    }





}