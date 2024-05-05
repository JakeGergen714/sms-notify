package com.jake.datacorelib.serviceschedule.jpa;

import com.jake.datacorelib.servicetype.jpa.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;

@Entity
@Data
public class ServiceSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long serviceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant creationDateTime;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdateDateTime;

    @ManyToOne
    @JoinColumn(name="id")
    private ServiceType serviceType;

}
