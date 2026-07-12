package com.myalbum.common.storage.service;

import com.myalbum.common.conversion.HeifConversionService;
import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.FileStorage;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.exception.StorageError;
import com.myalbum.common.storage.repository.UploadRepository;
import com.myalbum.common.storage.service.dto.ImageUploadResponse;
import com.myalbum.common.storage.service.dto.PhotoResponse;
import com.myalbum.config.UploadProperties;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService {

    private static final String THUMBNAILS_PREFIX = "thumb_";

    private final FileStorage fileStorage;
    private final HeifConversionService heifConversionService;
    private final UploadRepository uploadRepository;
    private final UploadProperties uploadProperties;

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
                        String originalFilename = file.getOriginalFilename();

                        // HEIF 변환 여부 확인
                        boolean isHeif = heifConversionService.isHeif(originalFilename);

                        // HEIF 파일인 경우 JPG로 변환 후 업로드
                        Path convertPath = isHeif
                                ? convertHeifToJpgPath(file)
                                : null;
                        byte[] uploadFileBytes = isHeif
                                ? Files.readAllBytes(convertPath)
                                : file.getBytes();

                        // 확장자 추출
                        String fileExtension = isHeif
                                ? extractExtension(convertPath.getFileName().toString())
                                : extractExtension(originalFilename);

                        // 저장 시 파일명 결정 (HEIF 변환 시 변환된 파일명 사용, 그렇지 않으면 원본 파일명 사용)
                        String saveFileName = isHeif
                                ? convertPath.getFileName().toString()
                                : originalFilename;

                        Thumbnails.of(new ByteArrayInputStream(uploadFileBytes))
                                .scale(0.4)
                                .outputQuality(0.5)
                                .outputFormat(fileExtension)
                                .toOutputStream(output);
                        byte[] thumbnailBytes = output.toByteArray();

                        // 썸네일 이미지 저장
                        UploadFile savedThumbnailUploadFile = this.saveFileBytes(thumbnailBytes, THUMBNAILS_PREFIX + saveFileName);

                        // 원본 이미지 저장
                        UploadFile savedOriginImageUploadFile = isHeif ? this.saveFileBytes(uploadFileBytes, saveFileName) : this.saveFile(file);

                        uploadedFiles.add(new ImageUploadResponse(savedThumbnailUploadFile, savedOriginImageUploadFile));
                    } catch (IOException e) {
                        AppException.exception(StorageError.FAILED_STORE_FILE);
                    }
                }
        );

        return uploadedFiles;
    }

    /**
     * 사진 상세 조회
     *
     * @param imageId 이미지 ID
     * @return 업로드된 이미지 파일 정보
     */
    public PhotoResponse getPhoto(Long imageId) {
        UploadFile uploadFile = uploadRepository.findById(imageId)
                .orElseThrow(() -> AppException.exception(StorageError.FILE_NOT_FOUND));

        return PhotoResponse.fromUploadFileEntity(uploadFile);
    }

    /**
     * 파일 확장자 추출
     *
     * @param originalFilename 파일명
     * @return 파일 확장자
     */
    private String extractExtension(String originalFilename) {
        int idx = originalFilename.lastIndexOf('.');
        return idx == -1 ? "" : originalFilename.substring(idx + 1);
    }

    /**
     * HEIF 파일을 JPG로 변환하고 변환된 JPG 파일의 경로를 반환
     *
     * @param file HEIF 파일
     * @return 변환된 JPG 파일의 경로
     * @throws IOException 파일 변환 중 오류 발생 시
     */
    private Path convertHeifToJpgPath(MultipartFile file) throws IOException {
        UploadFile uploadFile = fileStorage.storeFile(file);

        return heifConversionService.convertToJpg(Path.of(uploadProperties.getUploadPath(), uploadFile.getUrl()));
    }

}
