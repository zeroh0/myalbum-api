package com.myalbum.photo.service.dto;

import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.photo.entity.Photo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PhotoListResponse {

    private Long id;

    private String title;

    private String description;

    private Integer displayOrder;

    private UploadFile thumbnailUploadFile;

    private UploadFile originUploadFile;

    private LocalDateTime createdAt;

    public static List<PhotoListResponse> fromPhotoEntities(List<Photo> photos) {
        return photos.stream()
                .map(photo -> PhotoListResponse.builder()
                        .id(photo.getId())
                        .title(photo.getTitle())
                        .description(photo.getDescription())
                        .displayOrder(photo.getDisplayOrder())
                        .thumbnailUploadFile(photo.getThumbnailUploadFile())
                        .originUploadFile(photo.getOriginUploadFile())
                        .createdAt(photo.getCreatedAt())
                        .build())
                .toList();
    }

}
