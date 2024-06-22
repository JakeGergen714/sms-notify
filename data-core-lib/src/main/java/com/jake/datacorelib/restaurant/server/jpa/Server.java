package com.jake.datacorelib.restaurant.server.jpa;

import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.servicetype.serviceschedule.jpa.ServiceSchedule;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id", nullable = false)
    private Long serverId;

    private String name;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "server_service_schedules",
            joinColumns = @JoinColumn(name = "serverId"),
            inverseJoinColumns = @JoinColumn(name = "service_schedule_id")
    )
    private List<ServiceSchedule> schedules;
}
