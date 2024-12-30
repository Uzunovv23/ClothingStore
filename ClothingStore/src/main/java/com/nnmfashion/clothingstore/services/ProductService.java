package com.nnmfashion.clothingstore.services;

import com.nnmfashion.clothingstore.dtos.ProductDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.entities.Product;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import com.nnmfashion.clothingstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepo = productRepository;
        this.categoryRepo = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public Product createProduct(ProductDto productDto) {
        // Fetch the Category by its ID
        Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        Product newProduct = new Product();
        newProduct.setName(productDto.getName());
        newProduct.setPrice(productDto.getPrice());
        newProduct.setCategory(category); // Set the category

        return productRepo.save(newProduct);
    }

    public Product updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));

        // Fetch the Category by its ID
        Category category = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        existingProduct.setName(productDto.getName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setCategory(category); // Update the category

        return productRepo.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        productRepo.delete(product);
    }
}