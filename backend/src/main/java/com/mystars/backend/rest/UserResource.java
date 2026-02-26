package com.mystars.backend.rest;

import com.mystars.backend.entity.User;
import com.mystars.backend.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for User operations.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    private UserService userService;
    
    @GET
    public List<User> findAll() {
        return userService.findAll();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return userService.findById(id)
            .map(user -> Response.ok(user).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @GET
    @Path("/role/{role}")
    public List<User> findByRole(@PathParam("role") String role) {
        User.UserRole userRole = User.UserRole.valueOf(role.toUpperCase());
        return userService.findByRole(userRole);
    }
    
    @POST
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
    public Response update(@PathParam("id") UUID id, User user) {
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
    public Response delete(@PathParam("id") UUID id) {
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
    public Response deactivate(@PathParam("id") UUID id) {
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
    public Response activate(@PathParam("id") UUID id) {
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
