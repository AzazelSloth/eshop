package com.mystars.backend.repository;

import com.mystars.backend.entity.Product;
import com.mystars.backend.entity.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Product entity operations.
 */
public interface ProductRepository extends BaseRepository<Product> {
    
    /**
     * Find product by SKU.
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Find products by category.
     */
    List<Product> findByCategory(Category category);
    
    /**
     * Find products by category ID.
     */
    List<Product> findByCategoryId(UUID categoryId);
    
    /**
     * Find active products.
     */
    List<Product> findByIsActiveTrue();
    
    /**
     * Find active products with stock.
     */
    List<Product> findByIsActiveTrueAndStockQuantityGreaterThan(Integer quantity);
    
    /**
     * Search products by name (case-insensitive).
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products by price range.
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find products by category and active status.
     */
    List<Product> findByCategoryAndIsActive(Category category, boolean isActive);
}
