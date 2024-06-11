package com.jake.restaurantservice.exception;

public class FloorMapItemNotFoundException extends ResourceNotFoundException {
    public FloorMapItemNotFoundException(Long floorMapId) {
        super("Floor Map with ID " + floorMapId + " could not be found.");
    }
}
