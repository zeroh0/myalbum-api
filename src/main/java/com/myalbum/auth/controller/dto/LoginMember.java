package com.myalbum.auth.controller.dto;

import com.myalbum.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginMember {

    private Long memberId;

    private String username;

    private String displayName;

    private String profileImageUrl;

    private String status;

    public static LoginMember fromEntity(Member member) {
        return LoginMember.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .displayName(member.getDisplayName())
                .profileImageUrl(member.getProfileImageUrl())
                .status(member.getStatus())
                .build();
    }

}
