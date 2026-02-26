package com.mystars.backend.service;

import com.mystars.backend.entity.Order;
import com.mystars.backend.entity.OrderItem;
import com.mystars.backend.entity.Product;
import com.mystars.backend.entity.User;
import com.mystars.backend.repository.OrderRepository;
import com.mystars.backend.repository.ProductRepository;
import com.mystars.backend.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for Order operations.
 */
@ApplicationScoped
public class OrderService {
    
    @Inject
    private OrderRepository orderRepository;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private ProductRepository productRepository;
    
    /**
     * Find order by ID.
     */
    public Optional<Order> findById(UUID id) {
        return orderRepository.findByIdWithItems(id);
    }
    
    /**
     * Find all orders.
     */
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    /**
     * Find orders by user.
     */
    public List<Order> findByUser(UUID userId) {
        return orderRepository.findByUserId(userId);
    }
    
    /**
     * Find orders by status.
     */
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    /**
     * Find recent orders.
     */
    public List<Order> findRecent() {
        return orderRepository.findTop100ByOrderByCreatedAtDesc();
    }
    
    /**
     * Create new order.
     */
    @Transactional
    public Order create(Order order, UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        
        // Validate and process items
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProduct().getId()));
            
            if (!product.getIsActive()) {
                throw new IllegalArgumentException("Product is not available: " + product.getName());
            }
            
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
            
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            item.setOrder(order);
            
            // Decrease stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        
        order.calculateTotal();
        return orderRepository.save(order);
    }
    
    /**
     * Update order status.
     */
    @Transactional
    public Order updateStatus(UUID id, Order.OrderStatus newStatus) {
        Order order = orderRepository.findByIdWithItems(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        
        Order.OrderStatus currentStatus = order.getStatus();
        
        // Validate status transition
        validateStatusTransition(currentStatus, newStatus);
        
        order.setStatus(newStatus);
        
        // Update timestamps based on status
        switch (newStatus) {
            case SHIPPED:
                order.setShippedDate(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredDate(LocalDateTime.now());
                break;
            case CANCELLED:
            case REFUNDED:
                // Restore stock
                restoreStock(order);
                break;
        }
        
        return orderRepository.save(order);
    }
    
    /**
     * Cancel order.
     */
    @Transactional
    public Order cancel(UUID id) {
        return updateStatus(id, Order.OrderStatus.CANCELLED);
    }
    
    /**
     * Validate status transition.
     */
    private void validateStatusTransition(Order.OrderStatus current, Order.OrderStatus next) {
        // Define valid transitions
        boolean valid = switch (current) {
            case PENDING -> next == Order.OrderStatus.CONFIRMED 
                         || next == Order.OrderStatus.CANCELLED;
            case CONFIRMED -> next == Order.OrderStatus.PROCESSING 
                           || next == Order.OrderStatus.CANCELLED;
            case PROCESSING -> next == Order.OrderStatus.SHIPPED 
                            || next == Order.OrderStatus.CANCELLED;
            case SHIPPED -> next == Order.OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED, REFUNDED -> false;
        };
        
        if (!valid) {
            throw new IllegalArgumentException("Invalid status transition from " + current + " to " + next);
        }
    }
    
    /**
     * Restore stock when order is cancelled/refunded.
     */
    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }
}
