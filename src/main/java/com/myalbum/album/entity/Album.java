package com.myalbum.album.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 앨범 엔티티
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(comment = "pk", nullable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(comment = "회원 ID", nullable = false, columnDefinition = "BIGINT")
    private Long memberId;

    @Column(comment = "앨범 제목", nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(comment = "앨범 설명", columnDefinition = "TEXT")
    private String description;

    @Column(comment = "앨범 커버 이미지 URL", columnDefinition = "VARCHAR(255)")
    private String coverImageUrl;

    @Column(comment = "조회수", columnDefinition = "INT")
    private int viewCount;

    @Column(comment = "상태", columnDefinition = "VARCHAR(20)")
    private String status;

    @Column(comment = "생성일시 ", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(comment = "수정일시 ", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(comment = "삭제일시", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

}
