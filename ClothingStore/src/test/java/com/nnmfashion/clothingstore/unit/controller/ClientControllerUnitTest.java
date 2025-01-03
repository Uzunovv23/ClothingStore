package com.nnmfashion.clothingstore.unit.controller;

import com.nnmfashion.clothingstore.controllers.ClientController;
import com.nnmfashion.clothingstore.dtos.ClientDto;
import com.nnmfashion.clothingstore.entities.Client;
import com.nnmfashion.clothingstore.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
public class ClientControllerUnitTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;

    private Client client;
    private ClientDto clientDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();

        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        clientDto = new ClientDto();
        clientDto.setName("John Doe");
        clientDto.setEmail("john.doe@example.com");
    }

    @Test
    void testGetAllClients() throws Exception {
        List<Client> clients = Arrays.asList(client);

        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void testGetClientById() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(client);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void testCreateClient() throws Exception {
        when(clientService.createClient(any(ClientDto.class))).thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(clientService, times(1)).createClient(any(ClientDto.class));
    }

    @Test
    void testUpdateClient() throws Exception {
        when(clientService.updateClient(eq(1L), any(ClientDto.class))).thenReturn(client);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(clientService, times(1)).updateClient(eq(1L), any(ClientDto.class));
    }

    @Test
    void testDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClientById(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isOk());

        verify(clientService, times(1)).deleteClientById(1L);
    }
}
