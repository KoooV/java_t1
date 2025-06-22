package ru.t1.java.demo.util;

import org.junit.jupiter.api.Test;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.model.Client;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    @Test
    void toEntity_shouldMapDtoToEntity() {
        ClientDto dto = ClientDto.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .build();

        Client entity = ClientMapper.toEntity(dto);

        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getMiddleName(), entity.getMiddleName());
    }

    @Test
    void toEntity_shouldHandleNullMiddleName() {
        ClientDto dto = ClientDto.builder()
                .id(2L)
                .firstName("Anna")
                .lastName("Petrova")
                .middleName(null)
                .build();

        Client entity = ClientMapper.toEntity(dto);

        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertNull(entity.getMiddleName());
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        Client entity = Client.builder()
                .firstName("Sergey")
                .lastName("Sidorov")
                .middleName("Petrovich")
                .build();

        ClientDto dto = ClientMapper.toDto(entity);

        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getMiddleName(), dto.getMiddleName());
    }
} 