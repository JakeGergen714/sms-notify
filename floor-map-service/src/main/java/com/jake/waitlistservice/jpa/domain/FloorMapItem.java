package com.jake.waitlistservice.jpa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity(name = "floor_map_item")
public class FloorMapItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column
    private BigInteger floorMapId;

    @Column
    private String name;

    @Column
    private int minTableSize;

    @Column
    private int maxTableSize;

    @Column
    private boolean isReservable;
}
