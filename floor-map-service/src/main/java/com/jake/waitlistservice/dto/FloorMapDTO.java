package com.jake.waitlistservice.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FloorMapDTO {
    private Long id;
    private Long businessId;
    private String name;
    private LocalDateTime serviceTimeStart;
    private LocalDateTime serviceTimeEnd;
    private List<FloorMapItemDTO> floorMapItems;
}
