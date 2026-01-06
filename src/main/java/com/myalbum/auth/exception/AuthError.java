package com.myalbum.auth.exception;

import com.myalbum.common.response.AppError;
import lombok.Getter;

@Getter
public enum AuthError implements AppError {
    INVALID_TOKEN("AUTH_0001", "auth.invalid.token"),
    EXPIRED_TOKEN("AUTH_0002", "auth.expired.token"),
    INVALID_REFRESH_TOKEN("AUTH_0003", "auth.invalid.refresh.token"),
    REFRESH_TOKEN_NOT_FOUND("AUTH_0004", "auth.refresh.token.not.found"),
    REFRESH_TOKEN_EXPIRED("AUTH_0005", "auth.refresh.token.expired"),
    UNAUTHORIZED("AUTH_0006", "auth.unauthorized"),
    INVALID_SIGNATURE("AUTH_0007", "auth.invalid.signature"),
    UNSUPPORTED_TOKEN("AUTH_0008", "auth.unsupported.token"),
    EMPTY_CLAIMS("AUTH_0009", "auth.empty.claims"),
    FORBIDDEN("AUTH_0010", "auth.forbidden"),
    EMPTY_TOKEN("AUTH_0011", "auth.empty.token");

    private final String errorCode;
    private final String errorKey;

    AuthError(String errorCode, String errorKey) {
        this.errorCode = errorCode;
        this.errorKey = errorKey;
    }
}
