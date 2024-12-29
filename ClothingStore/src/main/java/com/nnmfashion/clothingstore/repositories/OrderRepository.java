package com.nnmfashion.clothingstore.repositories;

import com.nnmfashion.clothingstore.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order getOrderById(long id);
}