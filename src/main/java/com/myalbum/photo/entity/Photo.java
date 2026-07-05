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

import java.time.LocalDate;
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
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Size(max = 500)
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
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
    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @OneToOne
    @JoinColumn(name = "image_id")
    private UploadFile uploadFile;

}

