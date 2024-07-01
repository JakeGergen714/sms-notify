package com.jake.datacorelib.waitlist.jpa;

import com.jake.datacorelib.guest.jpa.Guest;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.seatingassignment.jpa.SeatingAssignment;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "wait_list")
public class WaitListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name="guest_id")
    @Column
    private Guest guest;

    @OneToOne(mappedBy = "guest")
    private SeatingAssignment seatingAssignment;

    @Column
    private int partySize;

    @Column
    private List<String> notes;

    @Column
    private Integer quotedTimeMinutes;

    @CreationTimestamp
    @Column
    private LocalDateTime checkInTime;
}
