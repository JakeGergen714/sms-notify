package com.jake.datacorelib.restaurant.floormap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FloorMapItemDTO {
    private long floorMapItemId;

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
