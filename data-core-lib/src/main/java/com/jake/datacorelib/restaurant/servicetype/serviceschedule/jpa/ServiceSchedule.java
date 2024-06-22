package com.jake.datacorelib.restaurant.servicetype.serviceschedule.jpa;

import com.jake.datacorelib.restaurant.server.jpa.Server;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude="serviceType")
@ToString(exclude = "serviceType")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "service_type_id", "day_of_week" }) })
public class ServiceSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long serviceScheduleId;

    @Column(nullable = false, name="day_of_week")
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
    @JoinColumn(name="service_type_id")
    private ServiceType serviceType;

    @ManyToMany(mappedBy="schedules", fetch = FetchType.EAGER)
    private List<Server> servers;
}
