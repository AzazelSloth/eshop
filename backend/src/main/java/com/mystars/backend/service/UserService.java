package com.mystars.backend.service;

import com.mystars.backend.entity.User;
import com.mystars.backend.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for User operations.
 */
@ApplicationScoped
public class UserService {
    
    @Inject
    private UserRepository userRepository;
    
    /**
     * Find user by ID.
     */
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
    
    /**
     * Find user by email.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Find all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    /**
     * Find users by role.
     */
    public List<User> findByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Create new user.
     */
    @Transactional
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }
    
    /**
     * Update user.
     */
    @Transactional
    public User update(User user) {
        User existing = userRepository.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + user.getId()));
        
        // Don't allow email change if it would conflict
        if (!existing.getEmail().equals(user.getEmail()) 
            && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        existing.setEmail(user.getEmail());
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setPhone(user.getPhone());
        existing.setRole(user.getRole());
        existing.setIsActive(user.getIsActive());
        
        // Only update password if provided
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            existing.setPasswordHash(user.getPasswordHash());
        }
        
        return userRepository.save(existing);
    }
    
    /**
     * Delete user.
     */
    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        userRepository.delete(user);
    }
    
    /**
     * Deactivate user (soft delete).
     */
    @Transactional
    public User deactivate(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setIsActive(false);
        return userRepository.save(user);
    }
    
    /**
     * Activate user.
     */
    @Transactional
    public User activate(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setIsActive(true);
        return userRepository.save(user);
    }
    
    /**
     * Check if email exists.
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
