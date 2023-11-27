package com.jake.waitlistservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthorizationDTO {
    private String username;
    private List<String> roles;
}
