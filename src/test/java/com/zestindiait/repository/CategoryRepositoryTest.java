package com.zestindiait.repository;

import com.zestindiait.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void testSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryRepository.save(category);

        assertNotNull(savedCategory);
        assertEquals(1L, savedCategory.getId());
        assertEquals("Electronics", savedCategory.getName());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testFindById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryRepository.findById(1L);

        assertTrue(foundCategory.isPresent());
        assertEquals("Electronics", foundCategory.get().getName());

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Category> foundCategory = categoryRepository.findById(2L);

        assertFalse(foundCategory.isPresent());

        verify(categoryRepository, times(1)).findById(2L);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryRepository.deleteById(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Home Appliances");

        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category savedCategory = categoryRepository.save(updatedCategory);

        assertEquals("Home Appliances", savedCategory.getName());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
