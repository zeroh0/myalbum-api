package com.myalbum.photo.entity;

import com.myalbum.common.storage.entity.UploadFile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "title", length = 100)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @OneToOne
    @JoinColumn(name = "thumbnail_id", nullable = false)
    private UploadFile thumbnailUploadFile;

    @OneToOne
    @JoinColumn(name = "image_id", nullable = false)
    private UploadFile originUploadFile;

    public void updateDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

}

