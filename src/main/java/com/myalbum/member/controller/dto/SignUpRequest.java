package com.myalbum.member.controller.dto;

import com.myalbum.common.valid.RequiredField;
import com.myalbum.member.service.dto.SignUpDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @RequiredField(params = "member.field.email")
    private String email;

    @RequiredField(params = "member.field.password")
    private String password;

    @RequiredField(params = "member.field.passwordConfirm")
    private String passwordConfirm;

    @RequiredField(params = "member.field.username")
    private String username;

    public SignUpDto toServiceDto() {
        return SignUpDto.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .build();
    }

}
