package com.jake.waitlistservice.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class FloorMapDTO {
    private BigInteger id;
    private BigInteger businessId;
    private String name;
    private List<FloorMapItemDTO> floorMapItems;
}
