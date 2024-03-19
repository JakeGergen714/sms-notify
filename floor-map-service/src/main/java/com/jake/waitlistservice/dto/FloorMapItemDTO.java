package com.jake.waitlistservice.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class FloorMapItemDTO {
    private long id;

    private long floorMapId;

    private String name;
    private String tableType;
    private Integer xPosition;
    private Integer yPosition;
    private Integer minPartySize;
    private Integer maxPartySize;
    private boolean isReservable;
}
