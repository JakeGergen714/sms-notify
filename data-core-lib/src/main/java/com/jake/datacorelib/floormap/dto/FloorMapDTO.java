package com.jake.datacorelib.floormap.dto;

import lombok.Data;

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
