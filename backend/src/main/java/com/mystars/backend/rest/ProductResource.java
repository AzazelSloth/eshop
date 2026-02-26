package com.mystars.backend.rest;

import com.mystars.backend.entity.Product;
import com.mystars.backend.service.ProductService;
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
public class ProductResource {
    
    @Inject
    private ProductService productService;
    
    @GET
    public List<Product> findAll(
            @QueryParam("active") Boolean active,
            @QueryParam("category") UUID categoryId,
            @QueryParam("search") String search,
            @QueryParam("minPrice") BigDecimal minPrice,
            @QueryParam("maxPrice") BigDecimal maxPrice) {
        
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
    public Response findById(@PathParam("id") UUID id) {
        return productService.findById(id)
            .map(product -> Response.ok(product).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @GET
    @Path("/sku/{sku}")
    public Response findBySku(@PathParam("sku") String sku) {
        return productService.findBySku(sku)
            .map(product -> Response.ok(product).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @POST
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
    public Response update(@PathParam("id") UUID id, Product product) {
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
    public Response updateStock(@PathParam("id") UUID id, @QueryParam("quantity") int quantity) {
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
    public Response delete(@PathParam("id") UUID id) {
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
    public Response deactivate(@PathParam("id") UUID id) {
        try {
            Product deactivated = productService.deactivate(id);
            return Response.ok(deactivated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
}
