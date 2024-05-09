package com.jake.datacorelib.restaurant.jpa;

import com.jake.datacorelib.restaurant.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long restaurantId;

    @Column
    private Long businessId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant creationDateTime;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdateDateTime;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ServiceType> serviceTypes;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<FloorMap> floorMaps;

}
