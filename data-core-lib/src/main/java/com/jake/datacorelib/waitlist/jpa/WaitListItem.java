package com.jake.datacorelib.waitlist.jpa;

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

    @Column
    private String businessId;

    @Column
    private String customerName;

    @Column
    private int partySize;

    @Column
    private List<String> notes;

    @Column
    private Integer quotedTimeMinutes;

    @Column
    private Integer waitTimeMinutes;

    @CreationTimestamp
    @Column
    private LocalDateTime checkInTime;
}
