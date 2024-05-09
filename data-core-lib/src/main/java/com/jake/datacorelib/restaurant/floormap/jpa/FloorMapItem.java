package com.jake.datacorelib.restaurant.floormap.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "floor_map_item")
public class FloorMapItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long floorMapItemId;

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

    @ManyToOne
    @JoinColumn(name="floor_map_id")
    private FloorMap floorMap;
}
