package com.mystars.backend.rest;

import com.mystars.backend.entity.Product;
import com.mystars.backend.service.ProductService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for Product operations.
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products", description = "Product management endpoints")
public class ProductResource {
    
    @Inject
    private ProductService productService;
    
    @GET
    @Operation(summary = "Get all products", description = "Retrieve all products with optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    })
    public List<Product> findAll(
            @Parameter(description = "Filter by active status") @QueryParam("active") Boolean active,
            @Parameter(description = "Filter by category ID") @QueryParam("category") UUID categoryId,
            @Parameter(description = "Search by product name") @QueryParam("search") String search,
            @Parameter(description = "Minimum price") @QueryParam("minPrice") BigDecimal minPrice,
            @Parameter(description = "Maximum price") @QueryParam("maxPrice") BigDecimal maxPrice) {
        
        if (active != null && active) {
            return productService.findActive();
        }
        
        if (categoryId != null) {
            return productService.findByCategory(categoryId);
        }
        
        if (search != null && !search.isEmpty()) {
            return productService.searchByName(search);
        }
        
        if (minPrice != null && maxPrice != null) {
            return productService.findByPriceRange(minPrice, maxPrice);
        }
        
        return productService.findAll();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Response findById(@Parameter(description = "Product UUID") @PathParam("id") UUID id) {
        return productService.findById(id)
            .map(product -> Response.ok(product).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @GET
    @Path("/sku/{sku}")
    @Operation(summary = "Get product by SKU", description = "Retrieve a product by its SKU")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Response findBySku(@Parameter(description = "Product SKU") @PathParam("sku") String sku) {
        return productService.findBySku(sku)
            .map(product -> Response.ok(product).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @POST
    @Operation(summary = "Create product", description = "Create a new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public Response create(Product product) {
        try {
            Product created = productService.create(product);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Response update(@Parameter(description = "Product UUID") @PathParam("id") UUID id, Product product) {
        try {
            product.setId(id);
            Product updated = productService.update(product);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PATCH
    @Path("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Update the stock quantity of a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Response updateStock(
            @Parameter(description = "Product UUID") @PathParam("id") UUID id, 
            @Parameter(description = "Quantity to add (positive) or remove (negative)") @QueryParam("quantity") int quantity) {
        try {
            Product updated = productService.updateStock(id, quantity);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete product", description = "Delete a product permanently")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Response delete(@Parameter(description = "Product UUID") @PathParam("id") UUID id) {
        try {
            productService.delete(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PATCH
    @Path("/{id}/deactivate")
    @Operation(summary = "Deactivate product", description = "Deactivate a product (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deactivated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Response deactivate(@Parameter(description = "Product UUID") @PathParam("id") UUID id) {
        try {
            Product deactivated = productService.deactivate(id);
            return Response.ok(deactivated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
}
