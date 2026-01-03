package com.myalbum.member.controller.dto;

import com.myalbum.common.valid.RequiredField;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @RequiredField(params = "member.field.email", message = "input.please.enter")
    private String email;

    @RequiredField(params = "member.field.password", message = "input.please.enter")
    private String password;

}
