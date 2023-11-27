package com.jake.auth.dto;

import lombok.Data;

@Data
public class AuthTokenDTO {
    private final String accessToken;
    private final String refreshToken;
}
