package com.nnmfashion.clothingstore.unit.service;

import com.nnmfashion.clothingstore.dtos.ClientDto;
import com.nnmfashion.clothingstore.entities.Client;
import com.nnmfashion.clothingstore.mappers.GenericMapper;
import com.nnmfashion.clothingstore.repositories.ClientRepository;
import com.nnmfashion.clothingstore.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceUnitTest {

    @Mock
    private ClientRepository clientRepo;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientDto clientDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        clientDto = new ClientDto();
        clientDto.setName("John Doe");
        clientDto.setEmail("john.doe@example.com");
    }

    @Test
    void testGetAllClients() {
        when(clientRepo.findAll()).thenReturn(Arrays.asList(client));

        List<Client> clients = clientService.getAllClients();

        assertNotNull(clients);
        assertEquals(1, clients.size());
        assertEquals("John Doe", clients.get(0).getName());

        verify(clientRepo, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));

        Client fetchedClient = clientService.getClientById(1L);

        assertNotNull(fetchedClient);
        assertEquals("John Doe", fetchedClient.getName());
        assertEquals("john.doe@example.com", fetchedClient.getEmail());

        verify(clientRepo, times(1)).findById(1L);
    }

    @Test
    void testCreateClient() {
        when(clientRepo.save(any(Client.class))).thenReturn(client);

        Client createdClient = clientService.createClient(clientDto);

        assertNotNull(createdClient);
        assertEquals("John Doe", createdClient.getName());
        assertEquals("john.doe@example.com", createdClient.getEmail());

        verify(clientRepo, times(1)).save(any(Client.class));
    }

    @Test
    void testUpdateClient() {
        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepo.save(any(Client.class))).thenReturn(client);

        Client updatedClient = clientService.updateClient(1L, clientDto);

        assertNotNull(updatedClient);
        assertEquals("John Doe", updatedClient.getName());
        assertEquals("john.doe@example.com", updatedClient.getEmail());

        verify(clientRepo, times(1)).findById(1L);
        verify(clientRepo, times(1)).save(any(Client.class));
    }

    @Test
    void testDeleteClientById() {
        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientRepo).delete(client);

        clientService.deleteClientById(1L);

        verify(clientRepo, times(1)).findById(1L);
        verify(clientRepo, times(1)).delete(client);
    }
}
