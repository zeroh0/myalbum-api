package com.myalbum.common.storage.enums;

import lombok.Getter;

@Getter
public enum UploadFileStatus {
    UPLOADED("UPLOADED", "업로드"),
    CONFIRMED("CONFIRMED", "확정");

    private final String name;
    private final String description;

    UploadFileStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
