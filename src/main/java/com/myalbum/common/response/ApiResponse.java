package com.myalbum.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private List<FieldError> fieldErrors = new ArrayList<>();

    public ApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, int code, String message, T data, List<FieldError> fieldErrors) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.fieldErrors = fieldErrors;
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), "success", null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), "success", data));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String errorMessage) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(false, status.value(), errorMessage, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String errorMessage, BindingResult bindingResult) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(false, status.value(), errorMessage, null, FieldError.of(bindingResult)));
    }

    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String message;

        public FieldError(String field, String value, String message) {
            this.field = field;
            this.value = value;
            this.message = message;
        }

        public static List<FieldError> of(String field, String value, String message) {
            List<FieldError> errors = new ArrayList<>();
            errors.add(new FieldError(field, value, message));
            return errors;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    ))
                    .toList();
        }
    }

}
