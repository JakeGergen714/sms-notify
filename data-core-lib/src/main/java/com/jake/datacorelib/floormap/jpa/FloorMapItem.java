package com.jake.datacorelib.floormap.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "floor_map_item")
public class FloorMapItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
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
