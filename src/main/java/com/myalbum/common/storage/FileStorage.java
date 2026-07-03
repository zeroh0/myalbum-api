package com.myalbum.common.storage;

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
     * @return 저장된 파일의 경로
     */
    String storeFile(MultipartFile file);

    /**
     * 파일 삭제
     *
     * @param key 삭제할 파일의 키
     */
    void delete(String key);

}
