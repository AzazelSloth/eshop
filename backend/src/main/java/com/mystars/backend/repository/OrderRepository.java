package com.mystars.backend.repository;

import com.mystars.backend.entity.Order;
import com.mystars.backend.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Order entity operations.
 */
public interface OrderRepository extends BaseRepository<Order> {
    
    /**
     * Find orders by user.
     */
    List<Order> findByUser(User user);
    
    /**
     * Find orders by user ID.
     */
    List<Order> findByUserId(UUID userId);
    
    /**
     * Find orders by status.
     */
    List<Order> findByStatus(Order.OrderStatus status);
    
    /**
     * Find orders by user and status.
     */
    List<Order> findByUserAndStatus(User user, Order.OrderStatus status);
    
    /**
     * Find order by ID with items.
     */
    Optional<Order> findByIdWithItems(UUID id);
    
    /**
     * Find recent orders.
     */
    List<Order> findTop100ByOrderByCreatedAtDesc();
}
