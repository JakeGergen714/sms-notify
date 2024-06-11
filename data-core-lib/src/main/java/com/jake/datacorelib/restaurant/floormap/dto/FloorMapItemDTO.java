package com.jake.datacorelib.restaurant.floormap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class FloorMapItemDTO {
    private Long floorMapItemId;
    private Long floorMapId;

    private String name;
    private String tableType;
    @JsonProperty("xPosition")
    private Integer xPosition;
    @JsonProperty("yPosition")
    private Integer yPosition;
    private Integer minPartySize;
    private Integer maxPartySize;
    private boolean isReservable;
    private int chairCount;
    private boolean disableChairsOnTop;
    private boolean disableChairsOnBottom;
    private boolean disableChairsOnLeft;
    private boolean disableChairsOnRight;
}
