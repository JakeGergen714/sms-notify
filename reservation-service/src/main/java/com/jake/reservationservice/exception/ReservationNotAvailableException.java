package com.jake.reservationservice.exception;

public class ReservationNotAvailableException extends RuntimeException {
    public ReservationNotAvailableException(String message) {
        super(message);
    }
}
