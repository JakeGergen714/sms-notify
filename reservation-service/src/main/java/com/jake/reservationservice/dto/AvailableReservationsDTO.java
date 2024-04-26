package com.jake.reservationservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailableReservationsDTO {
    LocalDate reservationDay;
    int partySize;
}
