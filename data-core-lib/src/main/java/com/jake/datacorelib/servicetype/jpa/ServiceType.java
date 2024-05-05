package com.jake.datacorelib.servicetype.jpa;

import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.serviceschedule.jpa.ServiceSchedule;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Data
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant creationDateTime;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdateDateTime;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy="serviceType")
    private List<ServiceSchedule> serviceSchedules;
}
