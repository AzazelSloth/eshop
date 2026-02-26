package com.mystars.backend.service;

import com.mystars.backend.entity.Category;
import com.mystars.backend.entity.Product;
import com.mystars.backend.repository.CategoryRepository;
import com.mystars.backend.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for Product operations.
 */
@ApplicationScoped
public class ProductService {
    
    @Inject
    private ProductRepository productRepository;
    
    @Inject
    private CategoryRepository categoryRepository;
    
    /**
     * Find product by ID.
     */
    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }
    
    /**
     * Find product by SKU.
     */
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    /**
     * Find all products.
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    /**
     * Find active products.
     */
    public List<Product> findActive() {
        return productRepository.findByIsActiveTrue();
    }
    
    /**
     * Find products by category.
     */
    public List<Product> findByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    /**
     * Search products by name.
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Find products by price range.
     */
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    /**
     * Create new product.
     */
    @Transactional
    public Product create(Product product) {
        if (product.getSku() != null && productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists: " + product.getSku());
        }
        
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            product.setCategory(category);
        }
        
        return productRepository.save(product);
    }
    
    /**
     * Update product.
     */
    @Transactional
    public Product update(Product product) {
        Product existing = productRepository.findById(product.getId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + product.getId()));
        
        // Check SKU uniqueness
        if (product.getSku() != null && !product.getSku().equals(existing.getSku())) {
            if (productRepository.findBySku(product.getSku()).isPresent()) {
                throw new IllegalArgumentException("SKU already exists: " + product.getSku());
            }
        }
        
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setSku(product.getSku());
        existing.setImageUrl(product.getImageUrl());
        existing.setIsActive(product.getIsActive());
        existing.setWeight(product.getWeight());
        existing.setDimensions(product.getDimensions());
        
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            existing.setCategory(category);
        } else {
            existing.setCategory(null);
        }
        
        return productRepository.save(existing);
    }
    
    /**
     * Update stock quantity.
     */
    @Transactional
    public Product updateStock(UUID id, int quantity) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        
        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }
    
    /**
     * Delete product.
     */
    @Transactional
    public void delete(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        productRepository.delete(product);
    }
    
    /**
     * Deactivate product.
     */
    @Transactional
    public Product deactivate(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        product.setIsActive(false);
        return productRepository.save(product);
    }
}
