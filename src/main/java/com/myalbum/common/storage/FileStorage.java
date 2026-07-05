package com.myalbum.common.storage;

import com.myalbum.common.storage.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

    /**
     * 초기화
     */
    void init();

    /**
     * 파일 저장
     *
     * @param file 저장할 파일
     * @return 저장된 파일의 정보
     */
    UploadFile storeFile(MultipartFile file);

    /**
     * 파일 저장
     *
     * @param fileBytes        저장할 파일의 바이트 배열
     * @param originalFilename 원본 파일 이름
     * @return 저장된 파일의 정보
     */
    UploadFile storeFile(byte[] fileBytes, String originalFilename);

    /**
     * 파일 삭제
     *
     * @param key 삭제할 파일의 키
     */
    void delete(String key);

}
