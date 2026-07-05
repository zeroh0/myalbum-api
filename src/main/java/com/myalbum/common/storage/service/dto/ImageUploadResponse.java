package com.myalbum.common.storage.service.dto;

import com.myalbum.common.storage.entity.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageUploadResponse {

    private UploadFile thumbnailImageFile;

    private UploadFile originalImageFile;

}
