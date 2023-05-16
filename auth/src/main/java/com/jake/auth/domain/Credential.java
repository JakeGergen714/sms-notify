package com.jake.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity(name="credential")
public class Credential {
    @Id
    private String username;
    private String password;
}
