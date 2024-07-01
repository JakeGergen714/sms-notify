package com.jake.datacorelib.seatingassignment.jpa;

import com.jake.datacorelib.guest.jpa.Guest;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapItem;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.user.jpa.RoleType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SeatingAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatingAssignmentId;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name="guest_id")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name="floor_map_item_id")
    private FloorMapItem floorMapItem;

    @Enumerated
    private SeatingStatus statusIndicator;

}
