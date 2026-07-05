package com.myalbum.common.storage.service;

import com.myalbum.common.storage.FileStorage;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService {

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

}
