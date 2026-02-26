package com.mystars.backend.repository;

import com.mystars.backend.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserRepository.
 */
@ApplicationScoped
public class UserRepositoryImpl extends AbstractRepository<User> implements UserRepository {
    
    public UserRepositoryImpl() {
        super(User.class);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        
        Predicate emailPredicate = cb.equal(root.get("email"), email);
        cq.where(emailPredicate);
        
        TypedQuery<User> query = em.createQuery(cq);
        List<User> results = query.getResultList();
        
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    @Override
    public boolean existsByEmail(String email) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<User> root = cq.from(User.class);
        
        cq.select(cb.count(root));
        cq.where(cb.equal(root.get("email"), email));
        
        Long count = em.createQuery(cq).getSingleResult();
        return count > 0;
    }
    
    @Override
    public List<User> findByRole(User.UserRole role) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        
        cq.where(cb.equal(root.get("role"), role));
        cq.orderBy(cb.asc(root.get("createdAt")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<User> findByIsActiveTrue() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        
        cq.where(cb.isTrue(root.get("isActive")));
        cq.orderBy(cb.asc(root.get("createdAt")));
        
        return em.createQuery(cq).getResultList();
    }
    
    @Override
    public List<User> findByRoleAndIsActive(User.UserRole role, boolean isActive) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("role"), role));
        predicates.add(cb.equal(root.get("isActive"), isActive));
        
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("createdAt")));
        
        return em.createQuery(cq).getResultList();
    }
}
