package com.myalbum.photo.controller;

import com.myalbum.common.response.ApiResponse;
import com.myalbum.photo.service.PhotoService;
import com.myalbum.photo.service.dto.SavePhotoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    /**
     * 사진 저장
     *
     * @param albumId          앨범 ID
     * @param savePhotoRequest 저장할 사진 정보
     */
    @PostMapping("/{albumId}")
    public ResponseEntity<ApiResponse<Void>> savePhoto(
            @PathVariable Long albumId,
            @RequestBody SavePhotoRequest savePhotoRequest
    ) {
        photoService.savePhoto(albumId, savePhotoRequest);

        return ApiResponse.ok();
    }

}
