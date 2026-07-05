package com.myalbum.common.storage.controller;

import com.myalbum.common.response.ApiResponse;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

}
