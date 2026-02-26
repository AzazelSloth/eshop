package com.mystars.backend.rest;

import com.mystars.backend.entity.User;
import com.mystars.backend.repository.UserRepository;
import com.mystars.backend.security.JwtService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller for Authentication operations.
 */
@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private JwtService jwtService;
    
    /**
     * Login request DTO.
     */
    public static class LoginRequest {
        public String email;
        public String password;
    }
    
    /**
     * Register request DTO.
     */
    public static class RegisterRequest {
        public String email;
        public String password;
        public String firstName;
        public String lastName;
        public String phone;
    }
    
    /**
     * Auth response DTO.
     */
    public static class AuthResponse {
        public String token;
        public String type = "Bearer";
        public UUID userId;
        public String email;
        public String role;
        
        public AuthResponse(String token, UUID userId, String email, String role) {
            this.token = token;
            this.userId = userId;
            this.email = email;
            this.role = role;
        }
    }
    
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request.email == null || request.password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Email and password are required"))
                .build();
        }
        
        User user = userRepository.findByEmail(request.email)
            .orElse(null);
        
        // In production, use BCrypt for password hashing
        if (user == null || !user.getPasswordHash().equals(request.password)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "Invalid credentials"))
                .build();
        }
        
        if (!user.getIsActive()) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", "Account is disabled"))
                .build();
        }
        
        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        
        AuthResponse response = new AuthResponse(token, user.getId(), user.getEmail(), user.getRole().name());
        return Response.ok(response).build();
    }
    
    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        if (request.email == null || request.password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "Email and password are required"))
                .build();
        }
        
        if (userRepository.existsByEmail(request.email)) {
            return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", "Email already exists"))
                .build();
        }
        
        User user = new User();
        user.setEmail(request.email);
        user.setPasswordHash(request.password); // In production, hash this with BCrypt
        user.setFirstName(request.firstName);
        user.setLastName(request.lastName);
        user.setPhone(request.phone);
        user.setRole(User.UserRole.CUSTOMER);
        user.setIsActive(true);
        
        User created = userRepository.save(user);
        
        String token = jwtService.generateToken(created.getId(), created.getEmail(), created.getRole().name());
        
        AuthResponse response = new AuthResponse(token, created.getId(), created.getEmail(), created.getRole().name());
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
    
    @GET
    @Path("/validate")
    public Response validate(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "Missing or invalid token"))
                .build();
        }
        
        String token = authHeader.substring(7);
        
        if (!jwtService.validateToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "Invalid token"))
                .build();
        }
        
        UUID userId = jwtService.getUserId(token);
        String email = jwtService.getEmail(token);
        String role = jwtService.getRole(token);
        
        return Response.ok(Map.of(
            "userId", userId.toString(),
            "email", email,
            "role", role
        )).build();
    }
}
