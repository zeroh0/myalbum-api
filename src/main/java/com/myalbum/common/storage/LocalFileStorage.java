package com.myalbum.common.storage;

import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.enums.UploadFileStatus;
import com.myalbum.common.storage.exception.StorageError;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class LocalFileStorage implements FileStorage {

    private final Path rootLocation;

    public LocalFileStorage(FileStorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            AppException.exception(StorageError.FILE_STORAGE_INIT_ERROR);
        }
    }

    @Override
    public UploadFile storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                AppException.exception(StorageError.FILE_STORAGE_EMPTY_FILE);
            }

            String extension = extractExtension(file.getOriginalFilename());
            String storedFilename = UUID.randomUUID() + extension;

            // 오늘 날짜 기준으로 경로 생성
            Path dateDirectory = this.rootLocation.resolve(LocalDate.now().toString());
            // 저장할 파일 경로 생성
            Path destinationFile = dateDirectory.resolve(storedFilename).normalize().toAbsolutePath();

            if (!destinationFile.startsWith(this.rootLocation)) {
                AppException.exception(StorageError.FAILED_STORE_FILE);
            }

            // 오늘 날짜 기준 디렉토리 생성
            Files.createDirectories(dateDirectory);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            String url = this.rootLocation.relativize(destinationFile).toString();

            return UploadFile.builder()
                    .url(url)
                    .originFileName(file.getOriginalFilename())
                    .saveFileName(storedFilename)
                    .extension(extension)
                    .size(file.getSize())
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
            Path filePath = this.rootLocation.resolve(key).normalize().toAbsolutePath();

            if (!filePath.startsWith(this.rootLocation)) {
                AppException.exception(StorageError.FAILED_DELETE_FILE);
            }

            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            AppException.exception(StorageError.FAILED_DELETE_FILE);
        }
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
