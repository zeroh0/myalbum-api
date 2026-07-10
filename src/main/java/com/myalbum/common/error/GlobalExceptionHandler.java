package com.myalbum.common.error;

import com.myalbum.auth.exception.AuthError;
import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.error.exception.CommonError;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.message.MessageResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("handleValidationException::", e);
        return ApiResponse.error(HttpStatus.BAD_REQUEST, MessageResolver.getMessage(CommonError.VALIDATION_ERROR), e.getBindingResult());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException e) {
        if (isUnauthenticatedError(e.getErrorCode())) {
            log.warn("handleAppException:: {}", e.getErrorMessage());
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, e.getErrorMessage());
        }

        log.error("handleAppException::", e);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, MessageResolver.getMessage(CommonError.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("handleException::", e);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, MessageResolver.getMessage(CommonError.INTERNAL_SERVER_ERROR));
    }

    private boolean isUnauthenticatedError(String errorCode) {
        return errorCode.startsWith("AUTH_") && !errorCode.equals(AuthError.FORBIDDEN.getErrorCode());
    }

}
