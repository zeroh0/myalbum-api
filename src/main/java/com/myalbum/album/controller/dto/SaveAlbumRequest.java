package com.myalbum.album.controller.dto;

import lombok.Getter;

@Getter
public class SaveAlbumRequest {

    private String title;

    private String description;

    private SaveUploadFileRequest saveUploadFileRequest;

}
