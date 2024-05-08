package com.jake.datacorelib.reservation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class ReservationDTO {
    private long reservationId;
    private long restaurantId;
    private long guestId;
    private String partyName;
    private int partySize;
    private List<String> notes;
    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private boolean completedIndicator;
}
