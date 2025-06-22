//package ru.t1.java.demo.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.t1.java.demo.dto.ClientDto;
//import ru.t1.java.demo.model.Client;
//import ru.t1.java.demo.repository.ClientRepository;
//import ru.t1.java.demo.service.ClientService;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ClientServiceImpl implements ClientService {
//
//    private final ClientRepository clientRepository;
//
//    @Override
//    @Transactional(readOnly = true)
//    public ClientDto getClientById(Long id) {
//        Client client = clientRepository.findById(id)
//            .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + id));
//        return convertToDto(client);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ClientDto> getAllClients() {
//        return clientRepository.findAll().stream()
//            .map(this::convertToDto)
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public ClientDto createClient(ClientDto clientDto) {
//        Client client = convertToEntity(clientDto);
//        Client savedClient = clientRepository.save(client);
//        return convertToDto(savedClient);
//    }
//
//    @Override
//    @Transactional
//    public ClientDto updateClient(ClientDto clientDto, Long id) {
//        Client existingClient = clientRepository.findById(id)
//            .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + id));
//
//        existingClient.setFirstName(clientDto.getFirstName());
//        existingClient.setLastName(clientDto.getLastName());
//        existingClient.setMiddleName(clientDto.getMiddleName());
//
//        Client updatedClient = clientRepository.save(existingClient);
//        return convertToDto(updatedClient);
//    }
//
//    @Override
//    @Transactional
//    public void deleteClient(Long id) {
//        if (!clientRepository.existsById(id)) {
//            throw new NoSuchElementException("Client not found with id: " + id);
//        }
//        clientRepository.deleteById(id);
//    }
//
//    private ClientDto convertToDto(Client client) {
//        return ClientDto.builder()
//            .id(client.getId())
//            .firstName(client.getFirstName())
//            .lastName(client.getLastName())
//            .middleName(client.getMiddleName())
//            .clientId(client.getClientId())
//            .build();
//    }
//
//    private Client convertToEntity(ClientDto dto) {
//        Client client = new Client();
//        client.setFirstName(dto.getFirstName());
//        client.setLastName(dto.getLastName());
//        client.setMiddleName(dto.getMiddleName());
//        return client;
//    }
//}
