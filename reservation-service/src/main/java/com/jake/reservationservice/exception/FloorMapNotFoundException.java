package com.jake.reservationservice.exception;

public class FloorMapNotFoundException extends RuntimeException {
    public FloorMapNotFoundException(String message) {
        super(message);
    }
}
