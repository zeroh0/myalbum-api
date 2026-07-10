package com.myalbum.member.controller.dto;

import com.myalbum.common.valid.RequiredField;
import com.myalbum.member.service.dto.SignUpDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "{member.invalid.email}")
    @RequiredField(params = "member.field.email")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,24}$",
            message = "{member.invalid.password}"
    )
    @RequiredField(params = "member.field.password")
    private String password;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,24}$",
            message = "{member.invalid.password}"
    )
    @RequiredField(params = "member.field.passwordConfirm")
    private String passwordConfirm;

    @Pattern(
            regexp = "^[a-z0-9][a-z0-9_-]{1,18}[a-z0-9]$",
            message = "{member.invalid.username}"
    )
    @RequiredField(params = "member.field.username")
    private String username;

    public SignUpDto toServiceDto() {
        return SignUpDto.builder()
                .email(this.email)
                .password(this.password)
                .passwordConfirm(this.passwordConfirm)
                .username(this.username)
                .build();
    }

}
