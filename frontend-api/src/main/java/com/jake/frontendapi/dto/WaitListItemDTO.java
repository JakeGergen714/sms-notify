package com.jake.frontendapi.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WaitListItemDTO {
    private BigInteger id;
    private String customerName;
    private int partySize;
    private List<String> notes;
    private Integer quotedTimeMinutes;
    private Integer waitTimeMinutes;
    private LocalDateTime checkInTime;
}
