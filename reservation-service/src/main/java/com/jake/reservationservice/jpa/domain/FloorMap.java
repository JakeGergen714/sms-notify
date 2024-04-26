package com.jake.reservationservice.jpa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime serviceTimeStart;
    @Column
    private LocalDateTime serviceTimeEnd;
}
