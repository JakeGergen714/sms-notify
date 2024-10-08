package com.jake.datacorelib.restaurant.servicetype.jpa;

import com.jake.datacorelib.restaurant.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.servicetype.serviceschedule.jpa.ServiceSchedule;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude="restaurant")
@ToString(exclude = {"restaurant"})
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_type_id", nullable = false)
    private Long serviceTypeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant creationDateTime;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy="serviceType", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ServiceSchedule> serviceSchedules;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "service_type_floor_map",
            joinColumns = @JoinColumn(name = "serviceTypeId"),
            inverseJoinColumns = @JoinColumn(name = "floor_map_id")
    )
    private List<FloorMap> floorMaps;
}
