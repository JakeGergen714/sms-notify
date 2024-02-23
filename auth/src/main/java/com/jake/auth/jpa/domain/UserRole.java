package com.jake.auth.jpa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "user_role")
@Data
public class UserRole {
    @Id
    private Long id;

    private Long userId;

    private String role;

}
