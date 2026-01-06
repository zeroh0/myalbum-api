package com.myalbum.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private TokenResponse tokenResponse;

    private LoginMember loginMember;

}
