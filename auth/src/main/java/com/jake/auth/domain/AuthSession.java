package com.jake.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "auth_session")
@AllArgsConstructor
@NoArgsConstructor
public class AuthSession {
    @Id
    private String username;
    private String token;
}
