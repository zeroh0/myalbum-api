package com.myalbum.member.controller.dto;

import com.myalbum.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignUpResponse {

    private String email;

    private String username;

    public static SignUpResponse fromEntity(Member savedMember) {
        return SignUpResponse.builder()
                .email(savedMember.getEmail())
                .username(savedMember.getUsername())
                .build();
    }

}
