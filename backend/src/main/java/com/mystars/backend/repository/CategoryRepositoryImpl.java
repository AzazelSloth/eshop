package com.mystars.backend.repository;

import com.mystars.backend.entity.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of CategoryRepository.
 */
@ApplicationScoped
public class CategoryRepositoryImpl extends AbstractRepository<Category> implements CategoryRepository {
    
    public CategoryRepositoryImpl() {
        super(Category.class);
    }
    
    @Override
    public Optional<Category> findByName(String name) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        
        cq.where(cb.equal(root.get("name"), name));
        
        TypedQuery<Category> query = em.createQuery(cq);
        List<Category> results = query.getResultList();
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public List<Category> findByParentIsNull() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        
        cq.where(cb.isNull(root.get("parent")));
        cq.orderBy(cb.asc(root.get("displayOrder")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Category> findByIsActiveTrue() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        
        cq.where(cb.isTrue(root.get("isActive")));
        cq.orderBy(cb.asc(root.get("displayOrder")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Category> findByParentId(UUID parentId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        
        CriteriaQuery<Category> select = cq.select(root);
        select.where(cb.equal(root.get("parent").get("id"), parentId));
        select.orderBy(cb.asc(root.get("displayOrder")));
        
        return em.createQuery(select).getResultList();
    }
}
