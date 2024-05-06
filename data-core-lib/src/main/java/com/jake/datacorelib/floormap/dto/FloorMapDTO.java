package com.jake.datacorelib.floormap.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class FloorMapDTO {
    private Long floorMapId;
    private Long businessId;
    private String name;
    private List<FloorMapItemDTO> floorMapItems;
}
