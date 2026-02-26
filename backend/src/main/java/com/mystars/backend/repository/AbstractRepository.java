package com.mystars.backend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Abstract base repository implementation providing common CRUD operations.
 * Uses JPA with JTA transaction management (WildFly container-managed).
 *
 * @param <T> Entity type extending BaseEntity
 */
public abstract class AbstractRepository<T> implements BaseRepository<T> {
    
    @PersistenceContext
    protected EntityManager em;
    
    private final Class<T> entityClass;
    
    protected AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected EntityManager getEntityManager() {
        return em;
    }
    
    protected CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }
    
    @Override
    public Optional<T> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        T entity = em.find(entityClass, id);
        return Optional.ofNullable(entity);
    }
    
    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        cq.orderBy(cb.asc(root.get("createdAt")));
        
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }
    
    @Override
    public List<T> findAll(int page, int size) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        cq.orderBy(cb.asc(root.get("createdAt")));
        
        TypedQuery<T> query = em.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
    
    @Override
    public long count() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        return em.createQuery(cq).getSingleResult();
    }
    
    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        // Check if entity has an ID (is managed)
        try {
            var idMethod = entityClass.getMethod("getId");
            UUID id = (UUID) idMethod.invoke(entity);
            
            if (id == null) {
                // New entity - persist
                em.persist(entity);
                return entity;
            } else {
                // Existing entity - merge
                return em.merge(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving entity", e);
        }
    }
    
    @Override
    public void delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        // If entity is not managed, merge it first
        if (!em.contains(entity)) {
            entity = em.merge(entity);
        }
        
        em.remove(entity);
    }
    
    @Override
    public Optional<T> findByIdAndLock(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        
        T entity = em.find(entityClass, id, LockModeType.PESSIMISTIC_WRITE);
        return Optional.ofNullable(entity);
    }
    
    /**
     * Execute a query with a custom CriteriaQuery.
     */
    protected List<T> executeQuery(CriteriaQuery<T> query) {
        return em.createQuery(query).getResultList();
    }
    
    /**
     * Create a typed query from CriteriaQuery.
     */
    protected TypedQuery<T> createQuery(CriteriaQuery<T> query) {
        return em.createQuery(query);
    }
}
