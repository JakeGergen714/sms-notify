package com.jake.waitlistservice.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class FloorMapDTO {
    private Long id;
    private Long businessId;
    private String name;
    private LocalTime serviceTimeStart;
    private LocalTime serviceTimeEnd;
    private List<FloorMapItemDTO> floorMapItems;
}
