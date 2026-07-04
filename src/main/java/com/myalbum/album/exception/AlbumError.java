package com.myalbum.album.exception;

import com.myalbum.common.response.AppError;
import lombok.Getter;

@Getter
public enum AlbumError implements AppError {
    ALBUM_NOT_FOUND("ALBUM_0001", "album.not.found"),
    UPLOAD_FILE_NOT_FOUND("ALBUM_0002", "upload.file.not.found");

    private final String errorCode;
    private final String errorKey;

    AlbumError(String errorCode, String errorKey) {
        this.errorCode = errorCode;
        this.errorKey = errorKey;
    }

}
