package com.nnmfashion.clothingstore.unit.service;

import com.nnmfashion.clothingstore.dtos.ProductDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.entities.Product;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import com.nnmfashion.clothingstore.repositories.ProductRepository;
import com.nnmfashion.clothingstore.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void testGetAllProducts() {
        when(productRepo.findAll()).thenReturn(Arrays.asList(product));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("T-Shirt", products.get(0).getName());

        verify(productRepo, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        Product fetchedProduct = productService.getProductById(1L);

        assertNotNull(fetchedProduct);
        assertEquals("T-Shirt", fetchedProduct.getName());
        assertEquals(19.99, fetchedProduct.getPrice());

        verify(productRepo, times(1)).findById(1L);
    }

    @Test
    void testCreateProduct() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(productRepo.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(productDto);

        assertNotNull(createdProduct);
        assertEquals("T-Shirt", createdProduct.getName());
        assertEquals(19.99, createdProduct.getPrice());
        assertEquals("Clothing", createdProduct.getCategory().getName());

        verify(categoryRepo, times(1)).findById(1L);
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(productRepo.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, productDto);

        assertNotNull(updatedProduct);
        assertEquals("T-Shirt", updatedProduct.getName());
        assertEquals(19.99, updatedProduct.getPrice());
        assertEquals("Clothing", updatedProduct.getCategory().getName());

        verify(productRepo, times(1)).findById(1L);
        verify(categoryRepo, times(1)).findById(1L);
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProductById() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepo).delete(product);

        productService.deleteProductById(1L);

        verify(productRepo, times(1)).findById(1L);
        verify(productRepo, times(1)).delete(product);
    }
}
