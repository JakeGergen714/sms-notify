package com.jake.restaurantservice.exception;

public class ServerNotFoundException extends ResourceNotFoundException {
    public ServerNotFoundException(Long floorMapId) {
        super("Server with ID " + floorMapId + " could not be found.");
    }
}
