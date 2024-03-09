package com.jake.waitlistservice.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class FloorMapItemDTO {
    private BigInteger id;

    private BigInteger floorMapId;

    private String name;
    private int minPartySize;
    private int maxPartySize;
    private boolean isReservable;
}
