package com.myalbum.common.storage;

import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.enums.UploadFileStatus;
import com.myalbum.common.storage.exception.StorageError;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDate;
import java.util.UUID;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class S3FileStorage implements FileStorage {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    @Override
    public void init() {
        // S3 버킷은 사전에 프로비저닝되어 있다고 가정하므로 별도 초기화가 필요 없음
    }

    @Override
    public UploadFile storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                AppException.exception(StorageError.FILE_STORAGE_EMPTY_FILE);
            }

            return storeFile(file.getBytes(), file.getOriginalFilename());
        } catch (Exception exception) {
            AppException.exception(StorageError.FAILED_STORE_FILE);
        }

        return null;
    }

    @Override
    public UploadFile storeFile(byte[] fileBytes, String originalFilename) {
        try {
            if (fileBytes.length == 0) {
                AppException.exception(StorageError.FILE_STORAGE_EMPTY_FILE);
            }

            String extension = extractExtension(originalFilename);
            String storedFilename = UUID.randomUUID() + "." + extension;

            // 오늘 날짜 기준으로 키 생성
            String key = LocalDate.now() + "/" + storedFilename;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(key)
                    .contentType(resolveContentType(extension))
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

            String url = buildPublicUrl(key);

            ImageDimensionExtractor.ImageDimension dimension = null;
            ExifExtractor.PhotoExifInfo exifInfo = null;
            if (!extension.equalsIgnoreCase("heic") && !extension.equalsIgnoreCase("heif")) {
                // 이미지 차원 추출 (width, height)
                dimension = ImageDimensionExtractor.extract(fileBytes);

                // 사진의 메타 데이터 추출 (카메라 모델, 렌즈 모델, ISO, 조리개, 셔터 속도, 촬영일)
                exifInfo = ExifExtractor.extract(fileBytes);
            }

            return UploadFile.builder()
                    .url(url)
                    .originFileName(originalFilename)
                    .saveFileName(storedFilename)
                    .extension(extension)
                    .size((long) fileBytes.length)
                    .width(dimension != null ? dimension.width() : null)
                    .height(dimension != null ? dimension.height() : null)
                    .cameraModel(exifInfo != null ? exifInfo.cameraModel() : null)
                    .lensModel(exifInfo != null ? exifInfo.lensModel() : null)
                    .iso(exifInfo != null ? exifInfo.iso() : null)
                    .aperture(exifInfo != null ? exifInfo.aperture() : null)
                    .shutterSpeed(exifInfo != null ? exifInfo.shutterSpeed() : null)
                    .takenAt(exifInfo != null ? exifInfo.takenAt() : null)
                    .status(UploadFileStatus.UPLOADED)
                    .build();
        } catch (Exception exception) {
            AppException.exception(StorageError.FAILED_STORE_FILE);
        }

        return null;
    }

    @Override
    public void delete(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(extractKey(key))
                    .build());
        } catch (Exception exception) {
            AppException.exception(StorageError.FAILED_DELETE_FILE);
        }
    }

    /**
     * S3 객체의 공개 URL 생성
     *
     * @param key 저장된 객체의 키
     * @return 공개 접근 가능한 URL
     */
    private String buildPublicUrl(String key) {
        return "https://" + s3Properties.getBucket() + ".s3." + s3Properties.getRegion() + ".amazonaws.com/" + key;
    }

    /**
     * 저장된 url(공개 URL 또는 키)에서 S3 객체 키를 추출
     *
     * @param urlOrKey 공개 URL 또는 키
     * @return S3 객체 키
     */
    private String extractKey(String urlOrKey) {
        String prefix = buildPublicUrl("");
        return urlOrKey.startsWith(prefix) ? urlOrKey.substring(prefix.length()) : urlOrKey;
    }

    /**
     * 확장자 기준 Content-Type 추론
     *
     * @param extension 파일 확장자
     * @return Content-Type
     */
    private String resolveContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "heic" -> "image/heic";
            case "heif" -> "image/heif";
            default -> "application/octet-stream";
        };
    }

    /**
     * 파일 확장자 추출
     *
     * @param filename 파일명
     * @return 파일 확장자
     */
    private String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex + 1) : "";
    }

}
