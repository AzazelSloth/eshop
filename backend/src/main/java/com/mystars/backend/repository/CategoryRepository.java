package com.mystars.backend.repository;

import com.mystars.backend.entity.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Category entity operations.
 */
public interface CategoryRepository extends BaseRepository<Category> {
    
    /**
     * Find category by name.
     */
    Optional<Category> findByName(String name);
    
    /**
     * Find root categories (categories without parent).
     */
    List<Category> findByParentIsNull();
    
    /**
     * Find active categories.
     */
    List<Category> findByIsActiveTrue();
    
    /**
     * Find categories by parent ID.
     */
    List<Category> findByParentId(UUID parentId);
}
