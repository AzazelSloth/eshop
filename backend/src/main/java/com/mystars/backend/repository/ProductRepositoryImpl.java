package com.mystars.backend.repository;

import com.mystars.backend.entity.Product;
import com.mystars.backend.entity.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of ProductRepository.
 */
@ApplicationScoped
public class ProductRepositoryImpl extends AbstractRepository<Product> implements ProductRepository {
    
    public ProductRepositoryImpl() {
        super(Product.class);
    }
    
    @Override
    public Optional<Product> findBySku(String sku) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        cq.where(cb.equal(root.get("sku"), sku));
        
        TypedQuery<Product> query = em.createQuery(cq);
        List<Product> results = query.getResultList();
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public List<Product> findByCategory(Category category) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        cq.where(cb.equal(root.get("category"), category));
        cq.orderBy(cb.asc(root.get("name")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Product> findByCategoryId(UUID categoryId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        CriteriaQuery<Product> select = cq.select(root);
        select.where(cb.equal(root.get("category").get("id"), categoryId));
        select.orderBy(cb.asc(root.get("name")));
        
        return em.createQuery(select).getResultList();
    }
    
    @Override
    public List<Product> findByIsActiveTrue() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        cq.where(cb.isTrue(root.get("isActive")));
        cq.orderBy(cb.asc(root.get("name")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Product> findByIsActiveTrueAndStockQuantityGreaterThan(Integer quantity) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isTrue(root.get("isActive")));
        predicates.add(cb.greaterThan(root.get("stockQuantity"), quantity));
        
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("name")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Product> findByNameContainingIgnoreCase(String name) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        cq.where(cb.like(cb.lower(root.get("name")), cb.lower(cb.literal("%" + name + "%"))));
        cq.orderBy(cb.asc(root.get("name")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        cq.where(cb.between(root.get("price"), minPrice, maxPrice));
        cq.orderBy(cb.asc(root.get("price")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Product> findByCategoryAndIsActive(Category category, boolean isActive) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("category"), category));
        predicates.add(cb.equal(root.get("isActive"), isActive));
        
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("name")));
        
        return em.createQuery(cq).getResultList();
    }
}
