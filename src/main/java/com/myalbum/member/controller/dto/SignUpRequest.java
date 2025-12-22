package com.myalbum.member.controller.dto;

import com.myalbum.member.service.dto.SignUpDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String passwordConfirm;

    @NotBlank(message = "사용자명은 필수 입력 항목입니다.")
    private String username;

    public SignUpDto toServiceDto() {
        return SignUpDto.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .build();
    }

}
