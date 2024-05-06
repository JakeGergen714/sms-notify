package com.jake.datacorelib.servicetype.jpa;

import com.jake.datacorelib.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.serviceschedule.jpa.ServiceSchedule;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude="restaurant")
@ToString(exclude = "restaurant")
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long serviceTypeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant creationDateTime;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdateDateTime;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy="serviceType", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ServiceSchedule> serviceSchedules;

    @ManyToOne
    @JoinColumn(name = "floor_map_id")
    private FloorMap floorMap;
}
