package com.jake.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "auth_session")
public class AuthSession {
    @Id
    private String username;
    private String token;
}
