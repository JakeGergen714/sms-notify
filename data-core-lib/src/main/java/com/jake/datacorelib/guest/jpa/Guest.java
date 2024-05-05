package com.jake.datacorelib.guest.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "guest")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long businessId;

    @Column(nullable = true)
    private Long tableId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private GuestStatus status;
}
