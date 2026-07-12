package com.myalbum.album.entity;

import com.myalbum.album.enums.AlbumStatus;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(comment = "앨범 제목", nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(comment = "앨범 설명", columnDefinition = "TEXT")
    private String description;

    @Column(comment = "조회수", columnDefinition = "INT")
    private int viewCount;

    @Column(comment = "상태", columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private AlbumStatus status;

    @CreationTimestamp
    @Column(comment = "생성일시 ", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(comment = "수정일시 ", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(comment = "삭제일시", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @OneToOne
    @JoinColumn(name = "image_id")
    private UploadFile uploadFile;

    public void delete() {
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = LocalDateTime.now();
    }

    public void update(String title, String description, UploadFile uploadFile) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (uploadFile != null) {
            this.uploadFile = uploadFile;
        }
        this.updatedAt = LocalDateTime.now();
    }

}
