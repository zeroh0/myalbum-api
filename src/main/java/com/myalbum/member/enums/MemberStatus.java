package com.myalbum.member.enums;

import lombok.Getter;

@Getter
public enum MemberStatus {
    PENDING("PENDING", "추가정보입력대기"),
    ACTIVE("ACTIVE", "활성"),
    ;

    private final String code;
    private final String description;

    MemberStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
