package com.jake.waitlistservice.jpa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity(name = "floor_map")
public class FloorMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long businessId;
    @Column
    private String name;
}
