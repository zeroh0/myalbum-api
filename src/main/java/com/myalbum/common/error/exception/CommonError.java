package com.myalbum.common.error.exception;

import com.myalbum.common.response.AppError;
import lombok.Getter;

@Getter
public enum CommonError implements AppError {
    INTERNAL_SERVER_ERROR("COMMON_001", "internal.server.error"),
    VALIDATION_ERROR("COMMON_002", "validation.error");

    private final String errorCode;
    private final String errorKey;

    CommonError(String errorCode, String errorKey) {
        this.errorCode = errorCode;
        this.errorKey = errorKey;
    }

}
