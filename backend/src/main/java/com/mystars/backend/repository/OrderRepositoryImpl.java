package com.mystars.backend.repository;

import com.mystars.backend.entity.Order;
import com.mystars.backend.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of OrderRepository.
 */
@ApplicationScoped
public class OrderRepositoryImpl extends AbstractRepository<Order> implements OrderRepository {
    
    public OrderRepositoryImpl() {
        super(Order.class);
    }
    
    @Override
    public List<Order> findByUser(User user) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        
        cq.where(cb.equal(root.get("user"), user));
        cq.orderBy(cb.desc(root.get("createdAt")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Order> findByUserId(UUID userId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        
        CriteriaQuery<Order> select = cq.select(root);
        select.where(cb.equal(root.get("user").get("id"), userId));
        select.orderBy(cb.desc(root.get("createdAt")));
        
        return em.createQuery(select).getResultList();
    }
    
    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        
        cq.where(cb.equal(root.get("status"), status));
        cq.orderBy(cb.desc(root.get("createdAt")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<Order> findByUserAndStatus(User user, Order.OrderStatus status) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        
        List<Predicate> predicates = new java.util.ArrayList<>();
        predicates.add(cb.equal(root.get("user"), user));
        predicates.add(cb.equal(root.get("status"), status));
        
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("createdAt")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public Optional<Order> findByIdWithItems(UUID id) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        
        root.fetch("items");
        root.fetch("user");
        
        cq.where(cb.equal(root.get("id"), id));
        
        TypedQuery<Order> query = em.createQuery(cq);
        List<Order> results = query.getResultList();
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public List<Order> findTop100ByOrderByCreatedAtDesc() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        
        cq.orderBy(cb.desc(root.get("createdAt")));
        
        TypedQuery<Order> query = em.createQuery(cq);
        query.setMaxResults(100);
        
        return query.getResultList();
    }
}
