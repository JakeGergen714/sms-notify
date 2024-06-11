package com.jake.datacorelib.restaurant.floormap.jpa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity(name = "floor_map_item")
@EqualsAndHashCode(exclude = "floorMap")
@ToString(exclude = "floorMap")
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

    @Column
    private int chairCount;

    @Column
    private boolean disableChairsOnTop;

    @Column
    private boolean disableChairsOnBottom;

    @Column
    private boolean disableChairsOnLeft;

    @Column
    private boolean disableChairsOnRight;

    @ManyToOne
    @JoinColumn(name="floor_map_id")
    private FloorMap floorMap;
}
