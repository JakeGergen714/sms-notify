package com.jake.datacorelib.waitlist.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WaitListItemDTO {
    private BigInteger id;
    private String customerName;
    private int partySize;
    private List<String> notes;
    private Integer quotedTimeMinutes;
    private Integer waitTimeMinutes;
    private LocalDateTime checkInTime;
}
