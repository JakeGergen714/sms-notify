package com.jake.reservationservice.exception;

public class NoAvailableReservationsException extends RuntimeException {
    public NoAvailableReservationsException(String message) {
        super(message);
    }
}
