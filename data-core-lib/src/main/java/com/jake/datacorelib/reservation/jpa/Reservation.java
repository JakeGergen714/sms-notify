package com.jake.datacorelib.reservation.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column
    private Long restaurantId;

    @Column
    private Long guestId;

    @Column
    private String partyName;

    @Column
    private Integer partySize;

    @Column
    private List<String> notes;

    @Column
    private LocalDate reservationDate;

    @Column
    private LocalTime reservationTime;

    @CreationTimestamp
    @Column
    private LocalDateTime createdDateTime;

    @Column
    private boolean completedIndicator;
}
