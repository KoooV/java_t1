package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.annotation.DataSourceError;
import ru.t1.java.demo.annotation.Metric;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Metric
    @DataSourceError
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Metric
    @DataSourceError
    public Client getClientById(UUID clientId) {
        return clientRepository.findByClientId(clientId);
    }

    @Metric
    @DataSourceError
    @Transactional
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Metric
    @DataSourceError
    @Transactional
    public Client updateClient(UUID clientId, Client clientDetails) {
        Client client = clientRepository.findByClientId(clientId);
        if (client != null) {
            client.setName(clientDetails.getName());
            client.setEmail(clientDetails.getEmail());
            return clientRepository.save(client);
        }
        return null;
    }

    @Metric
    @DataSourceError
    @Transactional
    public void deleteClient(UUID clientId) {
        Client client = clientRepository.findByClientId(clientId);
        if (client != null) {
            clientRepository.delete(client);
        }
    }
}
