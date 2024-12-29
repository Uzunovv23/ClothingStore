package com.nnmfashion.clothingstore.repositories;

import com.nnmfashion.clothingstore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category getCategoryById(long id);
}