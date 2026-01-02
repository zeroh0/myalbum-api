package com.myalbum.common.error.exception;

import com.myalbum.common.response.AppError;
import com.myalbum.common.response.MessageResolver;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public AppException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static AppException exception(AppError appError) {
        throw new AppException(appError.getErrorCode(), MessageResolver.getMessage(appError));
    }

    public static AppException exception(AppError appError, Object[] args) {
        throw new AppException(appError.getErrorCode(), MessageResolver.getMessage(appError, args));
    }

}
