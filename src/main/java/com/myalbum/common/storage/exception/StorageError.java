package com.myalbum.common.storage.exception;

import com.myalbum.common.response.AppError;
import lombok.Getter;

@Getter
public enum StorageError implements AppError {
    FILE_STORAGE_INIT_ERROR("STORAGE_0001", "file.storage.init.error"),
    FILE_STORAGE_EMPTY_FILE("STORAGE_0002", "file.storage.empty.file"),
    FAILED_STORE_FILE("STORAGE_0003", "file.storage.failed.store"),
    FAILED_DELETE_FILE("STORAGE_0004", "file.storage.failed.delete");

    private final String errorCode;
    private final String errorKey;

    StorageError(String errorCode, String errorKey) {
        this.errorCode = errorCode;
        this.errorKey = errorKey;
    }

}
