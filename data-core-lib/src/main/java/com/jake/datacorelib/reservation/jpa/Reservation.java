package com.jake.datacorelib.reservation.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long businessId;

    @Column
    private long guestId;

    @Column
    private String partyName;

    @Column
    private int partySize;

    @Column
    private List<String> notes;

    @CreationTimestamp
    @Column
    private LocalDateTime reservationTime;

    @Column
    private boolean completedIndicator;
}
