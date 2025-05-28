package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.ClientDto;
import java.util.List;

public interface ClientService {
    // Получить клиента по ID
    ClientDto getClientById(Long id);

    // Получить всех клиентов
    List<ClientDto> getAllClients();

    // Создать нового клиента
    ClientDto createClient(ClientDto clientDto);

    // Обновить существующего клиента
    ClientDto updateClient(ClientDto clientDto, Long id);

    // Удалить клиента
    void deleteClient(Long id);
}
