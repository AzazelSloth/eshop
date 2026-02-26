package com.mystars.backend.service;

import com.mystars.backend.entity.Category;
import com.mystars.backend.repository.CategoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for Category operations.
 */
@ApplicationScoped
public class CategoryService {
    
    @Inject
    private CategoryRepository categoryRepository;
    
    /**
     * Find category by ID.
     */
    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }
    
    /**
     * Find category by name.
     */
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    /**
     * Find all categories.
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    /**
     * Find active categories.
     */
    public List<Category> findActive() {
        return categoryRepository.findByIsActiveTrue();
    }
    
    /**
     * Find root categories.
     */
    public List<Category> findRootCategories() {
        return categoryRepository.findByParentIsNull();
    }
    
    /**
     * Find subcategories.
     */
    public List<Category> findSubcategories(UUID parentId) {
        return categoryRepository.findByParentId(parentId);
    }
    
    /**
     * Create new category.
     */
    @Transactional
    public Category create(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists: " + category.getName());
        }
        
        if (category.getParent() != null && category.getParent().getId() != null) {
            Category parent = categoryRepository.findById(category.getParent().getId())
                .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            category.setParent(parent);
        }
        
        return categoryRepository.save(category);
    }
    
    /**
     * Update category.
     */
    @Transactional
    public Category update(Category category) {
        Category existing = categoryRepository.findById(category.getId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + category.getId()));
        
        // Check name uniqueness
        if (!category.getName().equals(existing.getName()) 
            && categoryRepository.findByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists: " + category.getName());
        }
        
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setImageUrl(category.getImageUrl());
        existing.setIsActive(category.getIsActive());
        existing.setDisplayOrder(category.getDisplayOrder());
        
        if (category.getParent() != null && category.getParent().getId() != null) {
            // Prevent circular reference
            if (category.getParent().getId().equals(category.getId())) {
                throw new IllegalArgumentException("Category cannot be its own parent");
            }
            
            Category parent = categoryRepository.findById(category.getParent().getId())
                .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            existing.setParent(parent);
        } else {
            existing.setParent(null);
        }
        
        return categoryRepository.save(existing);
    }
    
    /**
     * Delete category.
     */
    @Transactional
    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        
        // Check if has children
        if (!category.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete category with subcategories");
        }
        
        categoryRepository.delete(category);
    }
}
