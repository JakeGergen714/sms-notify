package com.jake.auth.jpa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_cred")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCredential {
    @Id
    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    @JsonProperty("password") // Explicitly define the property name for Jackson
    private String password;
}
