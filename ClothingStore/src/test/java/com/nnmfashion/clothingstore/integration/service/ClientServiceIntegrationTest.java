package com.nnmfashion.clothingstore.integration.service;


import com.nnmfashion.clothingstore.dtos.ClientDto;
import com.nnmfashion.clothingstore.entities.Client;
import com.nnmfashion.clothingstore.repositories.ClientRepository;
import com.nnmfashion.clothingstore.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ClientServiceIntegrationTest {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll(); // Ensure a clean state
        // Add initial data
        Client client1 = new Client();
        client1.setName("John Doe");
        client1.setEmail("john@example.com");
        clientRepository.save(client1);

        Client client2 = new Client();
        client2.setName("Jane Doe");
        client2.setEmail("jane@example.com");
        clientRepository.save(client2);
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = clientService.getAllClients();
        assertEquals(2, clients.size());
    }

    @Test
    void testGetClientById() {
        Client savedClient = clientRepository.findAll().get(0); // Get any saved client
        Client fetchedClient = clientService.getClientById(savedClient.getId());
        assertNotNull(fetchedClient);
        assertEquals(savedClient.getName(), fetchedClient.getName());
    }

    @Test
    void testCreateClient() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName("New Client");
        clientDto.setEmail("newclient@example.com");

        Client createdClient = clientService.createClient(clientDto);
        assertNotNull(createdClient.getId());
        assertEquals("New Client", createdClient.getName());
    }

    @Test
    void testUpdateClient() {
        Client savedClient = clientRepository.findAll().get(0);

        ClientDto updateDto = new ClientDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@example.com");

        Client updatedClient = clientService.updateClient(savedClient.getId(), updateDto);
        assertEquals("Updated Name", updatedClient.getName());
        assertEquals("updated@example.com", updatedClient.getEmail());
    }

    @Test
    void testDeleteClientById() {
        Client savedClient = clientRepository.findAll().get(0);
        clientService.deleteClientById(savedClient.getId());
        assertFalse(clientRepository.existsById(savedClient.getId()));
    }
}