package com.nnmfashion.clothingstore.unit.service;

import com.nnmfashion.clothingstore.dtos.CategoryDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.repositories.CategoryRepository;
import com.nnmfashion.clothingstore.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Men's Clothing");

        categoryDto = new CategoryDto();
        categoryDto.setName("Men's Clothing");
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepo.findAll()).thenReturn(Arrays.asList(category));

        List<Category> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Men's Clothing", categories.get(0).getName());

        verify(categoryRepo, times(1)).findAll();
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(1L);

        assertNotNull(foundCategory);
        assertEquals("Men's Clothing", foundCategory.getName());

        verify(categoryRepo, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoryService.getCategoryById(1L));

        verify(categoryRepo, times(1)).findById(1L);
    }

    @Test
    void testCreateCategory() {
        when(categoryRepo.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category createdCategory = categoryService.createCategory(categoryDto);

        assertNotNull(createdCategory);
        assertEquals("Men's Clothing", createdCategory.getName());

        verify(categoryRepo, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepo.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        categoryDto.setName("Women's Clothing");
        Category updatedCategory = categoryService.updateCategory(1L, categoryDto);

        assertNotNull(updatedCategory);
        assertEquals(1L, updatedCategory.getId());
        assertEquals("Women's Clothing", updatedCategory.getName());

        verify(categoryRepo, times(1)).findById(1L);
        verify(categoryRepo, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryNotFound() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoryService.updateCategory(1L, categoryDto));

        verify(categoryRepo, times(1)).findById(1L);
    }

    @Test
    void testDeleteCategoryById() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepo, times(1)).findById(1L);
        verify(categoryRepo, times(1)).delete(category);
    }

    @Test
    void testDeleteCategoryByIdNotFound() {
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoryService.deleteCategoryById(1L));

        verify(categoryRepo, times(1)).findById(1L);
        verify(categoryRepo, never()).delete(any(Category.class));
    }
}
