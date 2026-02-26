package com.mystars.backend.rest;

import com.mystars.backend.entity.Category;
import com.mystars.backend.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for Category operations.
 */
@Path("/api/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
    
    @Inject
    private CategoryService categoryService;
    
    @GET
    public List<Category> findAll(@QueryParam("active") Boolean active) {
        if (active != null && active) {
            return categoryService.findActive();
        }
        return categoryService.findAll();
    }
    
    @GET
    @Path("/root")
    public List<Category> findRootCategories() {
        return categoryService.findRootCategories();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") UUID id) {
        return categoryService.findById(id)
            .map(category -> Response.ok(category).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @GET
    @Path("/{id}/children")
    public List<Category> findSubcategories(@PathParam("id") UUID id) {
        return categoryService.findSubcategories(id);
    }
    
    @POST
    public Response create(Category category) {
        try {
            Category created = categoryService.create(category);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, Category category) {
        try {
            category.setId(id);
            Category updated = categoryService.update(category);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        try {
            categoryService.delete(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
}
