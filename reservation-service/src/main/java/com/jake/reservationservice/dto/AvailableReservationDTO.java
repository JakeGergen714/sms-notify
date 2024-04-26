package com.jake.reservationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailableReservationDTO {
    LocalDateTime reservationTime;
    int minPartySize;
    int maxPartySize;
}
