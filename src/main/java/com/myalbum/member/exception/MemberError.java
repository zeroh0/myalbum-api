package com.myalbum.member.exception;

import com.myalbum.common.response.AppError;
import lombok.Getter;

@Getter
public enum MemberError implements AppError {
    MEMBER_NOT_FOUND("0001", "member.not.found"),
    DUPLICATE_EMAIL("0002", "member.duplicate.email"),
    DUPLICATE_USERNAME("0003", "member.duplicate.username"),
    PASSWORD_NOT_MATCH("0004", "member.not.match.password"),
    PASSWORD_CONFIRM_NOT_MATCH("0005", "member.not.match.password.confirm"),
    ALREADY_COMPLETED_ONBOARDING("0006", "member.already.completed.onboarding");

    private final String errorCode;
    private final String errorKey;

    MemberError(String errorCode, String errorKey) {
        this.errorCode = errorCode;
        this.errorKey = errorKey;
    }
}
