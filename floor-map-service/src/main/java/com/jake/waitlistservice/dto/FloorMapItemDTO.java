package com.jake.waitlistservice.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class FloorMapItemDTO {
    private long id;

    private long floorMapId;

    private String name;
    private int minPartySize;
    private int maxPartySize;
    private boolean isReservable;
}
