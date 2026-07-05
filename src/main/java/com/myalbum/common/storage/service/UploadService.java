package com.myalbum.common.storage.service;

import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.FileStorage;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.exception.StorageError;
import com.myalbum.common.storage.repository.UploadRepository;
import com.myalbum.common.storage.service.dto.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService {

    private static final String THUMBNAILS_PREFIX = "thumb_";

    private final FileStorage fileStorage;
    private final UploadRepository uploadRepository;

    /**
     * 파일 저장
     *
     * @param file 저장할 파일
     * @return 업로드된 파일 정보
     */
    public UploadFile saveFile(MultipartFile file) {
        // 파일 물리적 저장
        UploadFile uploadFile = fileStorage.storeFile(file);

        // 파일 정보 DB 저장
        return uploadRepository.save(uploadFile);
    }

    /**
     * 파일 저장 (바이트 배열)
     *
     * @param fileBytes        저장할 파일의 바이트 배열
     * @param originalFilename 원본 파일 이름
     * @return 업로드된 파일 정보
     */
    public UploadFile saveFileBytes(byte[] fileBytes, String originalFilename) {
        // 파일 물리적 저장
        UploadFile uploadFile = fileStorage.storeFile(fileBytes, originalFilename);

        // 파일 정보 DB 저장
        return uploadRepository.save(uploadFile);
    }

    /**
     * 여러 파일 저장
     *
     * @param files 저장할 파일 목록
     * @return 업로드된 파일 정보 목록
     */
    public List<UploadFile> saveFile(List<MultipartFile> files) {
        List<UploadFile> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadedFiles.add(saveFile(file));
        }
        return uploadedFiles;
    }

    /**
     * 이미지 파일을 받아서 썸네일용 이미지와 원본 이미지를 저장하고 업로드된 파일 정보를 반환
     *
     * @param files 업로드할 이미지 파일 목록
     * @return 업로드된 이미지 파일 정보 목록
     */
    public List<ImageUploadResponse> saveImageFiles(List<MultipartFile> files) {
        List<ImageUploadResponse> uploadedFiles = new ArrayList<>();

        files.forEach(file -> {
                    // 썸네일용 이미지 생성
                    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                        // 확장자 추출
                        String originalFilename = file.getOriginalFilename();
                        int dotIndex = originalFilename.lastIndexOf('.');
                        String fileExtension = (dotIndex >= 0) ? originalFilename.substring(dotIndex + 1) : "";

                        Thumbnails.of(new ByteArrayInputStream(file.getBytes()))
                                .scale(0.4)
                                .outputQuality(0.5)
                                .outputFormat(fileExtension)
                                .toOutputStream(output);
                        byte[] thumbnailBytes = output.toByteArray();

                        // 썸네일 이미지 저장
                        UploadFile savedThumbnailUploadFile = this.saveFileBytes(thumbnailBytes, THUMBNAILS_PREFIX + file.getOriginalFilename());

                        // 원본 이미지 저장
                        UploadFile savedOriginImageUploadFile = this.saveFile(file);

                        uploadedFiles.add(new ImageUploadResponse(savedThumbnailUploadFile, savedOriginImageUploadFile));
                    } catch (IOException e) {
                        AppException.exception(StorageError.FAILED_STORE_FILE);
                    }
                }
        );

        return uploadedFiles;
    }

}
