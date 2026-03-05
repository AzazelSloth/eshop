package com.mystars.backend.rest;

import com.mystars.backend.entity.Order;
import com.mystars.backend.service.OrderService;
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
 * REST controller for Order operations.
 */
@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderResource {
    
    @Inject
    private OrderService orderService;
    
    @GET
    @Operation(summary = "Get all orders", description = "Retrieve all orders with optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    public List<Order> findAll(
            @Parameter(description = "Filter by order status") @QueryParam("status") String status,
            @Parameter(description = "Filter by user ID") @QueryParam("userId") UUID userId,
            @Parameter(description = "Get recent orders") @QueryParam("recent") Boolean recent) {
        
        if (status != null) {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            return orderService.findByStatus(orderStatus);
        }
        
        if (userId != null) {
            return orderService.findByUser(userId);
        }
        
        if (recent != null && recent) {
            return orderService.findRecent();
        }
        
        return orderService.findAll();
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve an order by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public Response findById(@Parameter(description = "Order UUID") @PathParam("id") UUID id) {
        return orderService.findById(id)
            .map(order -> Response.ok(order).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @POST
    @Operation(summary = "Create order", description = "Create a new order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public Response create(Order order, @Parameter(description = "User UUID") @QueryParam("userId") UUID userId) {
        try {
            Order created = orderService.create(order, userId);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @PATCH
    @Path("/{id}/status")
    @Operation(summary = "Update order status", description = "Update the status of an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public Response updateStatus(
            @Parameter(description = "Order UUID") @PathParam("id") UUID id, 
            @Parameter(description = "New order status") @QueryParam("status") String status) {
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            Order updated = orderService.updateStatus(id, newStatus);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
    
    @POST
    @Path("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order cancelled successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public Response cancel(@Parameter(description = "Order UUID") @PathParam("id") UUID id) {
        try {
            Order cancelled = orderService.cancel(id);
            return Response.ok(cancelled).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
}
