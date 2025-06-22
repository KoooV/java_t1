package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.aspectspringbootstarter.annotation.DataSourceError;
import org.example.aspectspringbootstarter.annotation.Metric;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.List;
import java.util.Optional;
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
    public Client getClientById(UUID id) {
        return clientRepository.findByClientId(id).orElse(null);
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
    public Client updateClient(Client clientDetails, UUID clientId) {
        Client client = clientRepository.findByClientId(clientId).orElse(null);
        if (client != null) {
            client.setFirstName(clientDetails.getFirstName());
            client.setLastName(clientDetails.getLastName());
            client.setMiddleName(clientDetails.getMiddleName());
            return clientRepository.save(client);
        }
        return null;
    }

    @Metric
    @DataSourceError
    @Transactional
    public void deleteClient(UUID clientId) {
        Client client = clientRepository.findByClientId(clientId).orElse(null);
        if (client != null) {
            clientRepository.delete(client);
        }
    }
}
