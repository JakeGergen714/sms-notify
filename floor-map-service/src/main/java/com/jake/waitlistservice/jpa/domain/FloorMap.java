package com.jake.waitlistservice.jpa.domain;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity(name = "floor_map")
public class FloorMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column
    private BigInteger businessId;
    @Column
    private String name;
}
