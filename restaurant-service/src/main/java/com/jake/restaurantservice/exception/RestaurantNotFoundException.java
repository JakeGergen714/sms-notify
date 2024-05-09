package com.jake.restaurantservice.exception;

public class RestaurantNotFoundException extends ResourceNotFoundException {
    public RestaurantNotFoundException(Long restaurantId) {
        super("Restaurant with ID " + restaurantId + " could not be found.");
    }
}
