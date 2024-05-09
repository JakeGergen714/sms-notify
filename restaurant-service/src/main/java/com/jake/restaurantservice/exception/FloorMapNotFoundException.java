package com.jake.restaurantservice.exception;

public class FloorMapNotFoundException extends ResourceNotFoundException {
    public FloorMapNotFoundException(Long floorMapId) {
        super("Floor Map with ID " + floorMapId + " could not be found.");
    }
}
