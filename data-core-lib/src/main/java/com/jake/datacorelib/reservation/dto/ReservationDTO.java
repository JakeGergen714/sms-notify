package com.jake.datacorelib.reservation.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDTO {
    private long id;
    private long businessId;
    private long tableId;
    private String partyName;
    private int partySize;
    private List<String> notes;
    private LocalDateTime reservationTime;
    private boolean completedIndicator;
}
