package com.mystars.backend.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS Application configuration.
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // REST Resources
        classes.add(AuthResource.class);
        classes.add(UserResource.class);
        classes.add(ProductResource.class);
        classes.add(CategoryResource.class);
        classes.add(OrderResource.class);
        
        return classes;
    }
}
