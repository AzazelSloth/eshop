package com.mystars.backend.repository;

import com.mystars.backend.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations.
 */
public interface UserRepository extends BaseRepository<User> {
    
    /**
     * Find user by email.
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email.
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by role.
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * Find active users.
     */
    List<User> findByIsActiveTrue();
    
    /**
     * Find users by role and active status.
     */
    List<User> findByRoleAndIsActive(User.UserRole role, boolean isActive);
}
