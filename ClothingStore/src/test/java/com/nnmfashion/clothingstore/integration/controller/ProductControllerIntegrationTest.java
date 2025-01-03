package com.nnmfashion.clothingstore.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnmfashion.clothingstore.dtos.ProductDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.entities.Product;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import com.nnmfashion.clothingstore.repositories.ProductRepository;
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
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testGetAllProducts() throws Exception {
        Category category = new Category();
        category.setName("Men's Wear");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("T-Shirt");
        product.setPrice(19.99);
        product.setCategory(category);
        productRepository.save(product);

        mockMvc.perform(get("/api/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("T-Shirt"))
                .andExpect(jsonPath("$[0].price").value(19.99));
    }

    @Test
    void testGetProductById() throws Exception {
        Category category = new Category();
        category.setName("Women's Wear");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Dress");
        product.setPrice(49.99);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/api/products/{id}", savedProduct.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dress"))
                .andExpect(jsonPath("$.price").value(49.99));
    }

    @Test
    void testCreateProduct() throws Exception {
        Category category = new Category();
        category.setName("Kids' Wear");
        category = categoryRepository.save(category);

        ProductDto productDto = new ProductDto();
        productDto.setName("Sweater");
        productDto.setPrice(29.99);
        productDto.setCategoryId(category.getId());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Sweater"))
                .andExpect(jsonPath("$.price").value(29.99));

        Optional<Product> savedProduct = productRepository.findAll().stream().findFirst();
        assertThat(savedProduct).isPresent();
        assertThat(savedProduct.get().getName()).isEqualTo("Sweater");
        assertThat(savedProduct.get().getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    void testUpdateProduct() throws Exception {
        Category category = new Category();
        category.setName("Shoes");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Sneakers");
        product.setPrice(79.99);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        ProductDto updateDto = new ProductDto();
        updateDto.setName("Updated Sneakers");
        updateDto.setPrice(89.99);
        updateDto.setCategoryId(category.getId());

        mockMvc.perform(put("/api/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedProduct.getId()))
                .andExpect(jsonPath("$.name").value("Updated Sneakers"))
                .andExpect(jsonPath("$.price").value(89.99));

        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertThat(updatedProduct.getName()).isEqualTo("Updated Sneakers");
        assertThat(updatedProduct.getPrice()).isEqualTo(89.99);
    }

    @Test
    void testDeleteProduct() throws Exception {
        Category category = new Category();
        category.setName("Accessories");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Hat");
        product.setPrice(14.99);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(delete("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk());

        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertThat(deletedProduct).isEmpty();
    }
}