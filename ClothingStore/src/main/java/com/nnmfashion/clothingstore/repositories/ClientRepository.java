package com.nnmfashion.clothingstore.repositories;

import com.nnmfashion.clothingstore.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Client getClientById(long id);
}