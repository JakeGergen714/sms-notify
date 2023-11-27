package com.jake.auth.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class UserCredentialDTO {
    private final String username;
    @ToString.Exclude
    private final String password;
}
