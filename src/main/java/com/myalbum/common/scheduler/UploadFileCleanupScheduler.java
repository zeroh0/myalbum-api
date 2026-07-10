package com.myalbum.common.scheduler;

import com.myalbum.common.storage.FileStorage;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.enums.UploadFileStatus;
import com.myalbum.common.storage.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 고아 업로드 파일(앨범/사진에 확정되지 않고 방치된 파일) 정리 스케줄러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UploadFileCleanupScheduler {

    private static final long ORPHAN_FILE_RETENTION_DAYS = 7;

    private final UploadRepository uploadRepository;
    private final FileStorage fileStorage;

    /**
     * 매일 새벽 4시, 생성된 지 일주일이 지난 UPLOADED 상태의 고아 파일을 삭제
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void cleanupOrphanUploadFiles() {
        LocalDateTime expiredAt = LocalDateTime.now().minusDays(ORPHAN_FILE_RETENTION_DAYS);
        List<UploadFile> orphanFiles = uploadRepository.findByStatusAndCreatedAtBefore(UploadFileStatus.UPLOADED, expiredAt);

        if (orphanFiles.isEmpty()) {
            return;
        }

        List<UploadFile> deletableFiles = new ArrayList<>();
        for (UploadFile orphanFile : orphanFiles) {
            try {
                fileStorage.delete(orphanFile.getUrl());
                deletableFiles.add(orphanFile);
            } catch (Exception e) {
                log.warn("고아 파일 물리 삭제 실패. uploadFileId={}, url={}", orphanFile.getId(), orphanFile.getUrl(), e);
            }
        }

        uploadRepository.deleteAll(deletableFiles);
        log.info("고아 파일 정리 완료. 대상={}, 삭제={}", orphanFiles.size(), deletableFiles.size());
    }

}
