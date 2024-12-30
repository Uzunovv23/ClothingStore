package com.nnmfashion.clothingstore.services;

import com.nnmfashion.clothingstore.dtos.ClientDto;
import com.nnmfashion.clothingstore.entities.Client;
import com.nnmfashion.clothingstore.mappers.GenericMapper;
import com.nnmfashion.clothingstore.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepo;

    public List<Client> getAllClients() {
        return clientRepo.findAll().stream()
                .collect(Collectors.toList());
    }

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public Client getClientById(Long id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Client not found"));
        return client;
    }

    public Client createClient(ClientDto clientDto) {
        Client newClient = GenericMapper.dtoToEntity(clientDto, Client.class);
        Client savedClient = clientRepo.save(newClient);
        return savedClient;
    }

    public Client updateClient(Long id, ClientDto clientDto) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Client not found"));
        // Update only the fields coming from the DTO
        client = GenericMapper.dtoToEntity(clientDto, Client.class);
        client.setId(id);// This will update the existing client
        return clientRepo.save(client);
    }

    public void deleteClientById(Long id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Client not found"));
        clientRepo.delete(client);
    }
}