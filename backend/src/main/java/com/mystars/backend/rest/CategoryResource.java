package com.mystars.backend.rest;

import com.mystars.backend.entity.Category;
import com.mystars.backend.service.CategoryService;
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
 * REST controller for Category operations.
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Categories", description = "Product category management endpoints")
public class CategoryResource {
    
    @Inject
    private CategoryService categoryService;
    
    @GET
    @Operation(summary = "Get all categories", description = "Retrieve all categories with optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    })
    public List<Category> findAll(@Parameter(description = "Filter by active status") @QueryParam("active") Boolean active) {
        if (active != null && active) {
            return categoryService.findActive();
        }
        return categoryService.findAll();
    }
    
    @GET
    @Path("/root")
    @Operation(summary = "Get root categories", description = "Retrieve all root level categories (without parent)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Root categories retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    })
    public List<Category> findRootCategories() {
        return categoryService.findRootCategories();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a category by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public Response findById(@Parameter(description = "Category UUID") @PathParam("id") UUID id) {
        return categoryService.findById(id)
            .map(category -> Response.ok(category).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @GET
    @Path("/{id}/children")
    @Operation(summary = "Get subcategories", description = "Retrieve all subcategories of a category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subcategories retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    })
    public List<Category> findSubcategories(@Parameter(description = "Parent category UUID") @PathParam("id") UUID id) {
        return categoryService.findSubcategories(id);
    }
    
    @POST
    @Operation(summary = "Create category", description = "Create a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
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
    @Operation(summary = "Update category", description = "Update an existing category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public Response update(@Parameter(description = "Category UUID") @PathParam("id") UUID id, Category category) {
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
    @Operation(summary = "Delete category", description = "Delete a category permanently")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public Response delete(@Parameter(description = "Category UUID") @PathParam("id") UUID id) {
        try {
            categoryService.delete(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
}
