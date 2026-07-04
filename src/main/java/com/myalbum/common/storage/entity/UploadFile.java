package com.myalbum.common.storage.entity;

import com.myalbum.common.storage.enums.UploadFileStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 업로드 파일
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(comment = "pk", nullable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(comment = "파일 URL", nullable = false, columnDefinition = "VARCHAR(255)")
    private String url;

    @Column(comment = "원본 파일 이름", nullable = false, columnDefinition = "VARCHAR(255)")
    private String originFileName;

    @Column(comment = "저장된 파일 이름", nullable = false, columnDefinition = "VARCHAR(255)")
    private String saveFileName;

    @Column(comment = "파일 크기 (바이트)", nullable = false, columnDefinition = "BIGINT")
    private Long size;

    @Column(comment = "파일 확장자", nullable = false, columnDefinition = "VARCHAR(10)")
    private String extension;

    @CreationTimestamp
    @Column(comment = "생성일", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(comment = "상태", nullable = false, columnDefinition = "VARCHAR(20)")
    private UploadFileStatus status;

    public void confirmed() {
        this.status = UploadFileStatus.CONFIRMED;
    }

}
