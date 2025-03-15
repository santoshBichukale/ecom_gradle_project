package com.zestindiait.serviceImpl;

import com.zestindiait.entity.Category;
import com.zestindiait.repository.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics");
    }

    @Test
    void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);

        assertNotNull(createdCategory);
        assertEquals("Electronics", createdCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetCategoryById_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(1L);

        assertNotNull(foundCategory);
        assertEquals("Electronics", foundCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryById_WhenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                categoryService.getCategoryById(1L);
            }
        });

        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = Arrays.asList(category, new Category(2L, "Clothing"));
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory_WhenCategoryExists() {
        Category updatedCategory = new Category(1L, "Home Appliances");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(1L, updatedCategory);

        assertNotNull(result);
        assertEquals("Home Appliances", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_WhenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                categoryService.updateCategory(1L, category);
            }
        });

        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_WhenCategoryNotFound() {
        doThrow(new RuntimeException("Category not found")).when(categoryRepository).deleteById(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() {
                categoryService.deleteCategory(1L);
            }
        });

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
