package com.jake.reservationservice.exception;

public class TableReservedException extends RuntimeException {
    public TableReservedException(String message) {
        super(message);
    }
}
