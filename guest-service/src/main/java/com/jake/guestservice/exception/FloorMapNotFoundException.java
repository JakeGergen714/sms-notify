package com.jake.guestservice.exception;

public class FloorMapNotFoundException extends RuntimeException {
    public FloorMapNotFoundException(String message) {
        super(message);
    }
}
