package com.nnmfashion.clothingstore.unit.controller;

import com.nnmfashion.clothingstore.controllers.ProductController;
import com.nnmfashion.clothingstore.dtos.ProductDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.entities.Product;
import com.nnmfashion.clothingstore.services.ProductService;
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductControllerUnitTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private Product product;
    private ProductDto productDto;
    private Category category;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        category = new Category();
        category.setId(1L);
        category.setName("Clothing");

        product = new Product();
        product.setId(1L);
        product.setName("T-Shirt");
        product.setPrice(19.99);
        product.setCategory(category);

        productDto = new ProductDto();
        productDto.setName("T-Shirt");
        productDto.setPrice(19.99);
        productDto.setCategoryId(1L);
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(product);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("T-Shirt")))
                .andExpect(jsonPath("$[0].price", is(19.99)));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("T-Shirt")))
                .andExpect(jsonPath("$.price", is(19.99)));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"T-Shirt\", \"price\": 19.99, \"categoryId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("T-Shirt")))
                .andExpect(jsonPath("$.price", is(19.99)));

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"T-Shirt\", \"price\": 19.99, \"categoryId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("T-Shirt")))
                .andExpect(jsonPath("$.price", is(19.99)));

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProductById(1L);
    }
}
