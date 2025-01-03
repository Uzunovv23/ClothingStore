package com.nnmfashion.clothingstore.integration.service;

import com.nnmfashion.clothingstore.dtos.CategoryDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import com.nnmfashion.clothingstore.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll(); // Ensure a clean state

        // Add initial categories directly via repository
        Category category1 = new Category();
        category1.setName("Men's Clothing");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Women's Clothing");
        categoryRepository.save(category2);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        assertEquals(2, categories.size());
    }

    @Test
    void testGetCategoryById() {
        Category savedCategory = categoryRepository.findAll().get(0); // Get any saved category
        Category fetchedCategory = categoryService.getCategoryById(savedCategory.getId());
        assertNotNull(fetchedCategory);
        assertEquals(savedCategory.getName(), fetchedCategory.getName());
    }

    @Test
    void testCreateCategory() {
        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName("Kids' Clothing");

        Category createdCategory = categoryService.createCategory(newCategoryDto);
        assertNotNull(createdCategory.getId());
        assertEquals("Kids' Clothing", createdCategory.getName());
    }

    @Test
    void testUpdateCategory() {
        Category savedCategory = categoryRepository.findAll().get(0);

        CategoryDto updateDto = new CategoryDto();
        updateDto.setName("Updated Category");

        Category updatedCategory = categoryService.updateCategory(savedCategory.getId(), updateDto);
        assertEquals("Updated Category", updatedCategory.getName());
        assertEquals(savedCategory.getId(), updatedCategory.getId()); // Ensure the ID remains the same
    }

    @Test
    void testDeleteCategoryById() {
        Category savedCategory = categoryRepository.findAll().get(0);
        categoryService.deleteCategoryById(savedCategory.getId());
        assertFalse(categoryRepository.existsById(savedCategory.getId()));
    }
}