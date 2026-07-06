package com.myalbum.common.storage.entity;

import com.myalbum.common.storage.enums.UploadFileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Size(max = 100)
    @Column(name = "camera_model", nullable = false, length = 100)
    private String cameraModel;

    @Size(max = 100)
    @Column(name = "lens_model", nullable = false, length = 100)
    private String lensModel;

    @Column(name = "iso", nullable = false)
    private Integer iso;

    @Size(max = 10)
    @Column(name = "aperture", nullable = false, length = 10)
    private String aperture;

    @Size(max = 20)
    @Column(name = "shutter_speed", nullable = false, length = 20)
    private String shutterSpeed;

    @Column(name = "taken_at", nullable = false)
    private LocalDate takenAt;

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
