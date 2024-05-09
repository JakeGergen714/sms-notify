package com.jake.restaurantservice.exception;

public class ResourceNotFoundException extends RuntimeException{
   public ResourceNotFoundException(String message) {
        super(message);
    }
}
