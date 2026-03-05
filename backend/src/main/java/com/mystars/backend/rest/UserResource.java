package com.mystars.backend.rest;

import com.mystars.backend.entity.User;
import com.mystars.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for User operations.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "User management endpoints")
public class UserResource {
    
    @Inject
    private UserService userService;
    
    @GET
    @Operation(summary = "Get all users", description = "Retrieve all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    })
    public List<User> findAll() {
        return userService.findAll();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response findById(@Parameter(description = "User UUID") @PathParam("id") UUID id) {
        return userService.findById(id)
            .map(user -> Response.ok(user).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @GET
    @Path("/role/{role}")
    @Operation(summary = "Get users by role", description = "Retrieve all users with a specific role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    })
    public List<User> findByRole(@Parameter(description = "User role") @PathParam("role") String role) {
        User.UserRole userRole = User.UserRole.valueOf(role.toUpperCase());
        return userService.findByRole(userRole);
    }
    
    @POST
    @Operation(summary = "Create user", description = "Create a new user (admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public Response create(User user) {
        try {
            User created = userService.create(user);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response update(@Parameter(description = "User UUID") @PathParam("id") UUID id, User user) {
        try {
            user.setId(id);
            User updated = userService.update(user);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user permanently")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response delete(@Parameter(description = "User UUID") @PathParam("id") UUID id) {
        try {
            userService.delete(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PATCH
    @Path("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deactivated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response deactivate(@Parameter(description = "User UUID") @PathParam("id") UUID id) {
        try {
            User deactivated = userService.deactivate(id);
            return Response.ok(deactivated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PATCH
    @Path("/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User activated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response activate(@Parameter(description = "User UUID") @PathParam("id") UUID id) {
        try {
            User activated = userService.activate(id);
            return Response.ok(activated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    /**
     * Error response DTO.
     */
    public static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
    }
}
