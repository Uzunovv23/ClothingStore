package com.nnmfashion.clothingstore.repositories;

import com.nnmfashion.clothingstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public Product getProductById(long id);
}