package com.mystars.backend.rest;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OpenAPI resource to dynamically generate and serve the API documentation.
 * Uses Swagger annotations from the REST resources to build the spec.
 */
@Path("/openapi")
public class OpenAPIResource {

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenAPI() {
        try {
            // Get the base URL from the request
            String baseUrl = getBaseUrl();
            
            // Build the OpenAPI model
            OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                    .title("ShopOne E-Commerce API")
                    .description("REST API for ShopOne E-Commerce Platform. " +
                        "This API provides endpoints for managing products, categories, orders, and users. " +
                        "Authentication is required for most endpoints using JWT Bearer token.")
                    .version("v1")
                    .contact(new Contact()
                        .name("ShopOne Team")
                        .email("support@shopone.com")
                        .url("https://shopone.com"))
                    .license(new License()
                        .name("Proprietary")
                        .url("https://shopone.com/license")))
                .servers(List.of(
                    new Server()
                        .url(baseUrl + "/api")
                        .description("API Endpoint")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT token obtained from /auth/login or /auth/register endpoints")));

            // Create a Reader to scan JAX-RS annotations
            Reader reader = new Reader(openAPI);
            
            // Get all resource classes from ApplicationConfig
            Set<Class<?>> resourceClasses = getResourceClasses();
            
            // Read the OpenAPI spec from the annotations
            openAPI = reader.read(resourceClasses);

            // Convert to JSON
            String json = Json.pretty().writeValueAsString(openAPI);
            
            return Response.ok(json)
                .type(MediaType.APPLICATION_JSON)
                .build();
        } catch (Exception e) {
            return Response.serverError()
                .entity("{\"error\": \"Failed to generate OpenAPI specification: " + e.getMessage() + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @GET
    @Path("/yaml")
    @Produces("application/yaml")
    public Response getOpenAPIYaml() {
        try {
            String baseUrl = getBaseUrl();
            
            OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                    .title("ShopOne E-Commerce API")
                    .description("REST API for ShopOne E-Commerce Platform. " +
                        "This API provides endpoints for managing products, categories, orders, and users. " +
                        "Authentication is required for most endpoints using JWT Bearer token.")
                    .version("v1")
                    .contact(new Contact()
                        .name("ShopOne Team")
                        .email("support@shopone.com")
                        .url("https://shopone.com"))
                    .license(new License()
                        .name("Proprietary")
                        .url("https://shopone.com/license")))
                .servers(List.of(
                    new Server()
                        .url(baseUrl + "/api")
                        .description("API Endpoint")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT token obtained from /auth/login or /auth/register endpoints")));

            Reader reader = new Reader(openAPI);
            Set<Class<?>> resourceClasses = getResourceClasses();
            openAPI = reader.read(resourceClasses);

            String yaml = io.swagger.v3.core.util.Yaml.pretty().writeValueAsString(openAPI);
            
            return Response.ok(yaml)
                .type("application/yaml")
                .build();
        } catch (Exception e) {
            return Response.serverError()
                .entity("# Error generating OpenAPI specification\nerror: \"" + e.getMessage() + "\"")
                .type("application/yaml")
                .build();
        }
    }

    private Set<Class<?>> getResourceClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Add all REST resource classes
        classes.add(AuthResource.class);
        classes.add(UserResource.class);
        classes.add(ProductResource.class);
        classes.add(CategoryResource.class);
        classes.add(OrderResource.class);
        
        return classes;
    }

    private String getBaseUrl() {
        if (uriInfo != null) {
            return String.valueOf(uriInfo.getBaseUri());
        }
        return "";
    }
}
