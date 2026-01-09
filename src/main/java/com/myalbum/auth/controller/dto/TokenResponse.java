package com.myalbum.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;

    private Long accessTokenExpiresIn;

    private String tokenType;

}
