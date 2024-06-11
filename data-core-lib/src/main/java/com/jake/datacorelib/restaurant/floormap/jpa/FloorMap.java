package com.jake.datacorelib.restaurant.floormap.jpa;

import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@Entity(name = "floor_map")
@EqualsAndHashCode(exclude = "restaurant")
@ToString(exclude = {"restaurant", "serviceTypes"})
public class FloorMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long floorMapId;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy="floorMap", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<FloorMapItem> floorMapItems;

    @ManyToMany(mappedBy = "floorMaps")
    private List<ServiceType> serviceTypes;
}
