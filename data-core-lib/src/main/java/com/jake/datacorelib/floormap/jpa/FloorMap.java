package com.jake.datacorelib.floormap.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity(name = "floor_map")
public class FloorMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long businessId;
    @Column
    private String name;
    @Column
    private LocalTime serviceTimeStart;
    @Column
    private LocalTime serviceTimeEnd;
}
