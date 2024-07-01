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
    private String name;

    @Column(nullable = false)
    private String phone;
}
