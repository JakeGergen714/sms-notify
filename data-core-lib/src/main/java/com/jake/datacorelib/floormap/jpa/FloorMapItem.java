package com.jake.datacorelib.floormap.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "floor_map_item")
public class FloorMapItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long floorMapItemId;

    @Column(nullable = false)
    private long floorMapId;

    @Column
    private String name;

    @Column
    private String tableType;

    @Column
    private Integer xPosition;

    @Column
    Integer yPosition;

    @Column
    private Integer minTableSize;

    @Column
    private Integer maxTableSize;

    @Column
    private boolean isReservable;
}
