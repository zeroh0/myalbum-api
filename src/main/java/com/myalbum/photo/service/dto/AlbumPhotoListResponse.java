package com.myalbum.photo.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlbumPhotoListResponse {

    private Long albumId;

    private String title;

    private String description;

    private String status;

    private boolean owner;

    private List<PhotoListResponse> photos;

}
