package com.jake.waitlistservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class FloorMapItemDTO {
    private long id;

    private long floorMapId;

    private String name;
    private String tableType;
    @JsonProperty("xPosition")
    private Integer xPosition;
    @JsonProperty("yPosition")
    private Integer yPosition;
    private Integer minPartySize;
    private Integer maxPartySize;
    private boolean isReservable;
}
