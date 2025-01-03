package com.nnmfashion.clothingstore.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnmfashion.clothingstore.dtos.CategoryDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void testGetAllCategories() throws Exception {
        Category category = new Category();
        category.setName("Men's Clothing");
        categoryRepository.save(category);

        mockMvc.perform(get("/api/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Men's Clothing"));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = new Category();
        category.setName("Women's Clothing");
        Category savedCategory = categoryRepository.save(category);

        mockMvc.perform(get("/api/categories/{id}", savedCategory.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Women's Clothing"));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Kids' Clothing");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Kids' Clothing"));

        Optional<Category> savedCategory = categoryRepository.findAll().stream().findFirst();
        assertThat(savedCategory).isPresent();
        assertThat(savedCategory.get().getName()).isEqualTo("Kids' Clothing");
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = new Category();
        category.setName("Electronics");
        Category savedCategory = categoryRepository.save(category);

        CategoryDto updateDto = new CategoryDto();
        updateDto.setName("Updated Electronics");

        mockMvc.perform(put("/api/categories/{id}", savedCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value("Updated Electronics"));

        Category updatedCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow();
        assertThat(updatedCategory.getName()).isEqualTo("Updated Electronics");
    }

    @Test
    void testDeleteCategory() throws Exception {
        Category category = new Category();
        category.setName("Accessories");
        Category savedCategory = categoryRepository.save(category);

        mockMvc.perform(delete("/api/categories/{id}", savedCategory.getId()))
                .andExpect(status().isOk());

        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(deletedCategory).isEmpty();
    }
}