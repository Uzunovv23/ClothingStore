package com.nnmfashion.clothingstore.unit.controller;

import com.nnmfashion.clothingstore.controllers.CategoryController;
import com.nnmfashion.clothingstore.dtos.CategoryDto;
import com.nnmfashion.clothingstore.entities.Category;
import com.nnmfashion.clothingstore.services.CategoryService;
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
import static org.mockito.ArgumentMatchers.any; // Ensure this is imported for Mockito's any()
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerUnitTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        category = new Category();
        category.setId(1L);
        category.setName("Men's Clothing");

        categoryDto = new CategoryDto();
        categoryDto.setName("Men's Clothing");
    }

    @Test
    void testGetAllCategories() throws Exception {
        List<Category> categories = Arrays.asList(category);

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Men's Clothing")));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Men's Clothing")));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void testCreateCategory() throws Exception {
        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Men's Clothing\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Men's Clothing")));

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(CategoryDto.class))).thenReturn(category);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Men's Clothing\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Men's Clothing")));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(CategoryDto.class));
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategoryById(1L);
    }
}
