package com.myalbum.auth.controller.dto;

import com.myalbum.common.valid.RequiredField;
import lombok.Getter;

@Getter
public class TokenRefreshRequest {

    @RequiredField(message = "auth.empty.token")
    private String refreshToken;

}
