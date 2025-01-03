package com.nnmfashion.clothingstore.integration.service;

import com.nnmfashion.clothingstore.dtos.ProductDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.entities.Product;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import com.nnmfashion.clothingstore.repositories.ProductRepository;
import com.nnmfashion.clothingstore.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll(); // Clear previous products
        categoryRepository.deleteAll(); // Clear previous categories

        // Create a test category
        testCategory = new Category();
        testCategory.setName("Test Category");
        testCategory = categoryRepository.save(testCategory);

        // Add initial products
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(10.0);
        product1.setCategory(testCategory);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(20.0);
        product2.setCategory(testCategory);
        productRepository.save(product2);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void testGetProductById() {
        Product savedProduct = productRepository.findAll().get(0); // Get any saved product
        Product fetchedProduct = productService.getProductById(savedProduct.getId());
        assertNotNull(fetchedProduct);
        assertEquals(savedProduct.getName(), fetchedProduct.getName());
    }

    @Test
    void testCreateProduct() {
        ProductDto newProductDto = new ProductDto();
        newProductDto.setName("New Product");
        newProductDto.setPrice(30.0);
        newProductDto.setCategoryId(testCategory.getId()); // Set the existing test category

        Product createdProduct = productService.createProduct(newProductDto);
        assertNotNull(createdProduct.getId());
        assertEquals("New Product", createdProduct.getName());
        assertEquals(testCategory.getId(), createdProduct.getCategory().getId());
    }

    @Test
    void testUpdateProduct() {
        Product savedProduct = productRepository.findAll().get(0);

        ProductDto updateDto = new ProductDto();
        updateDto.setName("Updated Product");
        updateDto.setPrice(40.0);
        updateDto.setCategoryId(testCategory.getId()); // Use the same category

        Product updatedProduct = productService.updateProduct(savedProduct.getId(), updateDto);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(40.0, updatedProduct.getPrice());
        assertEquals(testCategory.getId(), updatedProduct.getCategory().getId());
    }

    @Test
    void testDeleteProductById() {
        Product savedProduct = productRepository.findAll().get(0);
        productService.deleteProductById(savedProduct.getId());
        assertFalse(productRepository.existsById(savedProduct.getId()));
    }
}