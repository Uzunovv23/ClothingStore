package com.nnmfashion.clothingstore.services;

import com.nnmfashion.clothingstore.dtos.CategoryDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.mappers.GenericMapper;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepo = categoryRepository;
    }
    public List<Category> getAllCategories() {
        return categoryRepo.findAll().stream()
                .collect(Collectors.toList());
    }

    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
    }

    public Category createCategory(CategoryDto categoryDto) {
        Category newCategory = GenericMapper.dtoToEntity(categoryDto, Category.class);
        return categoryRepo.save(newCategory);
    }

    public Category updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
        category = GenericMapper.dtoToEntity(categoryDto, Category.class);
        category.setId(id); // Set the existing category's ID to update it
        return categoryRepo.save(category);
    }

    public void deleteCategoryById(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found"));
        categoryRepo.delete(category);
    }
}