package com.myalbum.common.conversion.enums;

import com.myalbum.common.response.AppError;
import lombok.Getter;

@Getter
public enum HeifConversionError implements AppError {
    HEIF_CONVERSION_INTERRUPTED("HEIF_001", "heif.conversion.interrupted"),
    HEIF_CONVERSION_FAILED("HEIF_002", "heif.conversion.failed");

    private final String errorCode;
    private final String errorKey;

    HeifConversionError(String errorCode, String errorKey) {
        this.errorCode = errorCode;
        this.errorKey = errorKey;
    }

}
