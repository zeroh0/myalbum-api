package com.myalbum.member.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignUpDto {

    private String email;

    private String password;

    private String username;

}
