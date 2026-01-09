package com.myalbum.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtToken {

    private final String accessToken;

    private final String refreshToken;

    private final Long accessTokenExpiresIn;

    private final String tokenType;

    public static JwtToken of(String accessToken, String refreshToken, Long expiresIn) {
        return new JwtToken(accessToken, refreshToken, expiresIn, "Bearer");
    }

}
