package com.nnmfashion.clothingstore.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnmfashion.clothingstore.dtos.ClientDto;
import com.nnmfashion.clothingstore.entities.Client;
import com.nnmfashion.clothingstore.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    void testGetAllClients() throws Exception {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john@example.com");
        clientRepository.save(client);

        mockMvc.perform(get("/api/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void testGetClientById() throws Exception {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john@example.com");
        Client savedClient = clientRepository.save(client);

        mockMvc.perform(get("/api/clients/{id}", savedClient.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testCreateClient() throws Exception {
        ClientDto clientDto = new ClientDto();
        clientDto.setName("Jane Doe");
        clientDto.setEmail("jane@example.com");

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));

        Optional<Client> savedClient = clientRepository.findAll().stream().findFirst();
        assertTrue(savedClient.isPresent());
        assertEquals("Jane Doe", savedClient.get().getName());
    }

    @Test
    void testUpdateClient() throws Exception {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john@example.com");
        Client savedClient = clientRepository.save(client);

        ClientDto updateDto = new ClientDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@example.com");

        mockMvc.perform(put("/api/clients/{id}", savedClient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedClient.getId()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        Client updatedClient = clientRepository.findById(savedClient.getId()).orElseThrow();
        assertEquals("Updated Name", updatedClient.getName());
        assertEquals("updated@example.com", updatedClient.getEmail());
    }

    @Test
    void testDeleteClient() throws Exception {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john@example.com");
        Client savedClient = clientRepository.save(client);

        mockMvc.perform(delete("/api/clients/{id}", savedClient.getId()))
                .andExpect(status().isOk());

        assertFalse(clientRepository.existsById(savedClient.getId()));
    }
}