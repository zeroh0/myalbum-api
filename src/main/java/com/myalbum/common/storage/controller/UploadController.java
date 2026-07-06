package com.myalbum.common.storage.controller;

import com.myalbum.common.response.ApiResponse;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.service.UploadService;
import com.myalbum.common.storage.service.dto.ImageUploadResponse;
import com.myalbum.common.storage.service.dto.PhotoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    /**
     * 파일 업로드
     *
     * @param file 업로드할 파일
     * @return 업로드된 파일 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UploadFile>> uploadFile(
            MultipartFile file
    ) {
        UploadFile uploadFile = uploadService.saveFile(file);
        return ApiResponse.ok(uploadFile);
    }

    /**
     * 이미지 파일을 받아서 썸네일용 이미지와 원본 이미지를 저장하고 업로드된 파일 정보를 반환하는 API
     *
     * @param files 업로드할 이미지 파일 목록
     * @return 업로드된 이미지 파일 정보
     */
    @PostMapping("/images")
    public ResponseEntity<ApiResponse<List<ImageUploadResponse>>> uploadImageFile(
            List<MultipartFile> files
    ) {
        List<ImageUploadResponse> uploadFiles = uploadService.saveImageFiles(files);

        return ApiResponse.ok(uploadFiles);
    }

    /**
     * 사진 상세 조회
     *
     * @param imageId 사진 ID
     * @return 사진 상세 정보
     */
    @GetMapping("/{imageId}")
    public ResponseEntity<ApiResponse<PhotoResponse>> getPhoto(
            @PathVariable Long imageId
    ) {
        PhotoResponse photoResponse = uploadService.getPhoto(imageId);

        return ApiResponse.ok(photoResponse);
    }

}
