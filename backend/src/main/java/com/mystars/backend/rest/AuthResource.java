package com.mystars.backend.rest;

import com.mystars.backend.entity.User;
import com.mystars.backend.repository.UserRepository;
import com.mystars.backend.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthResource {
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private JwtService jwtService;
    
    /**
     * Login request DTO.
     */
    @Schema(description = "Login request payload")
    public static class LoginRequest {
        @Schema(description = "User email address", example = "user@example.com", required = true)
        public String email;
        @Schema(description = "User password", example = "password123", required = true)
        public String password;
    }
    
    /**
     * Register request DTO.
     */
    @Schema(description = "Registration request payload")
    public static class RegisterRequest {
        @Schema(description = "User email address", example = "user@example.com", required = true)
        public String email;
        @Schema(description = "User password", example = "password123", required = true)
        public String password;
        @Schema(description = "User first name", example = "John")
        public String firstName;
        @Schema(description = "User last name", example = "Doe")
        public String lastName;
        @Schema(description = "User phone number", example = "+1234567890")
        public String phone;
    }
    
    /**
     * Auth response DTO.
     */
    @Schema(description = "Authentication response")
    public static class AuthResponse {
        @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        public String token;
        @Schema(description = "Token type", example = "Bearer")
        public String type = "Bearer";
        @Schema(description = "User ID")
        public UUID userId;
        @Schema(description = "User email")
        public String email;
        @Schema(description = "User role")
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
    @Operation(summary = "User login", description = "Authenticate a user and return a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request - missing email or password"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "403", description = "Account is disabled")
    })
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
    @Operation(summary = "Register new user", description = "Register a new user in the system and return a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad request - missing email or password"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
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
    @Operation(summary = "Validate token", description = "Validate a JWT token and return user information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token is valid",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Missing or invalid token")
    })
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
