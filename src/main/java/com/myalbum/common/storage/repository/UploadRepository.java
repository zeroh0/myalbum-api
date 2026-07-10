package com.myalbum.common.storage.repository;

import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.enums.UploadFileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UploadRepository extends JpaRepository<UploadFile, Long> {

    /**
     * 상태 및 생성일 기준 파일 목록 조회
     *
     * @param status    파일 상태
     * @param createdAt 기준 생성일
     * @return 기준일 이전에 생성된 해당 상태의 파일 목록
     */
    List<UploadFile> findByStatusAndCreatedAtBefore(UploadFileStatus status, LocalDateTime createdAt);

}
