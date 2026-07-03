package com.myalbum.album.enums;

import lombok.Getter;

@Getter
public enum AlbumStatus {
    PUBLIC("공개"),
    PRIVATE("비공개");

    private final String description;

    AlbumStatus(String description) {
        this.description = description;
    }

}
