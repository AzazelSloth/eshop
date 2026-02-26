package com.mystars.backend.rest;

import com.mystars.backend.entity.Order;
import com.mystars.backend.service.OrderService;
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
public class OrderResource {
    
    @Inject
    private OrderService orderService;
    
    @GET
    public List<Order> findAll(
            @QueryParam("status") String status,
            @QueryParam("userId") UUID userId,
            @QueryParam("recent") Boolean recent) {
        
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
    public Response findById(@PathParam("id") UUID id) {
        return orderService.findById(id)
            .map(order -> Response.ok(order).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @POST
    public Response create(Order order, @QueryParam("userId") UUID userId) {
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
    public Response updateStatus(@PathParam("id") UUID id, @QueryParam("status") String status) {
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
    public Response cancel(@PathParam("id") UUID id) {
        try {
            Order cancelled = orderService.cancel(id);
            return Response.ok(cancelled).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new UserResource.ErrorResponse(e.getMessage())).build();
        }
    }
}
