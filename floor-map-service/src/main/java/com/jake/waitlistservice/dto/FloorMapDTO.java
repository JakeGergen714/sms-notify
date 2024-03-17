package com.jake.waitlistservice.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class FloorMapDTO {
    private Long id;
    private Long businessId;
    private String name;
    private List<FloorMapItemDTO> floorMapItems;
}
