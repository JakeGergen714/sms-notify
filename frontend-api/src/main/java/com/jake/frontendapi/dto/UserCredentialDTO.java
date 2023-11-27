package com.jake.frontendapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class UserCredentialDTO {
    private String username;
    @ToString.Exclude
    private String password;
}
