package ru.t1.java.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void getClientById_whenClientExists_shouldReturnClient() {
        // given
        UUID clientId = UUID.randomUUID();
        Client expectedClient = new Client();
        expectedClient.setClientId(clientId);

        when(clientRepository.findByClientId(clientId)).thenReturn(Optional.of(expectedClient));

        // when
        Client actualClient = clientService.getClientById(clientId);

        // then
        assertNotNull(actualClient);
        assertEquals(expectedClient, actualClient);
        verify(clientRepository).findByClientId(clientId);
    }

    @Test
    void getClientById_whenClientDoesNotExist_shouldReturnNull() {
        // given
        UUID clientId = UUID.randomUUID();
        when(clientRepository.findByClientId(clientId)).thenReturn(Optional.empty());

        // when
        Client actualClient = clientService.getClientById(clientId);

        // then
        assertNull(actualClient);
        verify(clientRepository).findByClientId(clientId);
    }

    @Test
    void createClient_shouldSaveAndReturnClient() {
        // given
        Client clientToSave = new Client();
        clientToSave.setFirstName("John");
        when(clientRepository.save(any(Client.class))).thenReturn(clientToSave);

        // when
        Client savedClient = clientService.createClient(clientToSave);

        // then
        assertNotNull(savedClient);
        assertEquals("John", savedClient.getFirstName());
        verify(clientRepository).save(clientToSave);
    }
} 