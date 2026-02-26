package com.mystars.backend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Base repository interface providing common CRUD operations.
 * Uses JPA Criteria API for type-safe queries.
 *
 * @param <T> Entity type
 */
public interface BaseRepository<T> {
    
    /**
     * Find entity by ID.
     */
    Optional<T> findById(UUID id);
    
    /**
     * Find all entities.
     */
    List<T> findAll();
    
    /**
     * Find entities with pagination.
     */
    List<T> findAll(int page, int size);
    
    /**
     * Count all entities.
     */
    long count();
    
    /**
     * Save (persist or merge) entity.
     */
    T save(T entity);
    
    /**
     * Delete entity.
     */
    void delete(T entity);
    
    /**
     * Find entity by ID and lock for update.
     */
    Optional<T> findByIdAndLock(UUID id);
}
