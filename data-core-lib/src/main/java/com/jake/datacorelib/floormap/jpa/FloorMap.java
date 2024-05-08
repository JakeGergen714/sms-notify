package com.jake.datacorelib.floormap.jpa;

import com.jake.datacorelib.serviceschedule.jpa.ServiceSchedule;
import com.jake.datacorelib.servicetype.jpa.ServiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "floor_map")
public class FloorMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long floorMapId;
    @Column
    private Long businessId;
    @Column
    private String name;

    @OneToMany(mappedBy="floorMap", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ServiceType> serviceTypes;

    @OneToMany(mappedBy="floorMap", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<FloorMapItem> floorMapItems;
}
