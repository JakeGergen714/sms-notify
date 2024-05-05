package com.jake.guestservice.exception;

public class TableReservedException extends RuntimeException {
    public TableReservedException(String message) {
        super(message);
    }
}
