package ru.t1.java.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.service.ClientService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void createClient_whenValidInput_shouldReturn201() throws Exception {
        // given
        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setMiddleName("Smith");


        Client client = new Client();
        client.setClientId(UUID.randomUUID());
        client.setFirstName("John");
        client.setLastName("Doe");

        ClientDto responseDto = new ClientDto();
        responseDto.setClientId(client.getClientId());
        responseDto.setFirstName(client.getFirstName());
        responseDto.setLastName(client.getLastName());

        when(modelMapper.map(any(ClientDto.class), any())).thenReturn(client);
        when(clientService.createClient(any(Client.class))).thenReturn(client);
        when(modelMapper.map(any(Client.class), any())).thenReturn(responseDto);


        // when & then
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.client_id").value(client.getClientId().toString()))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"));
    }
} 