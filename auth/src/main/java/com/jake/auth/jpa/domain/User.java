package com.jake.auth.jpa.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "user_creds")
@Data
public class User {

    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long businessId;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;
}
