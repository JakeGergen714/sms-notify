package com.jake.datacorelib.restaurant.floormap.dto;

import lombok.Data;

import java.util.List;

@Data
public class FloorMapDTO {
    private Long floorMapId;
    private Long businessId;
    private String name;
    private List<FloorMapItemDTO> floorMapItems;
}
