package com.zestindiait.serviceImpl;

import com.zestindiait.customeExceptions.CategoryNotFoundException;
import com.zestindiait.entity.Category;
import com.zestindiait.repository.CategoryRepository;
import com.zestindiait.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        System.out.println("Creating category: " + category.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        if (existingCategory != null) {
            existingCategory.setName(category.getName());
            return categoryRepository.save(existingCategory);
        }
        throw new CategoryNotFoundException("Category not found with id: " + id);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
