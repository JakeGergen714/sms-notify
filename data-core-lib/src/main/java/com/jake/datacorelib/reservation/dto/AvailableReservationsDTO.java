package com.jake.datacorelib.reservation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class AvailableReservationsDTO {
    private LocalDate reservationDate;
    private Set<LocalTime> reservationTimes;

}
