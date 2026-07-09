package com.myalbum.photo.repository;

import com.myalbum.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    /**
     * 앨범의 사진 목록을 표시 순서대로 조회
     *
     * @param albumId 앨범 ID
     * @return 표시 순서로 정렬된 사진 목록
     */
    List<Photo> findByAlbumIdOrderByDisplayOrderAsc(Long albumId);

    /**
     * 앨범 내 사진의 최대 표시 순서 조회 (사진이 없으면 0)
     *
     * @param albumId 앨범 ID
     * @return 최대 표시 순서
     */
    @Query("SELECT COALESCE(MAX(p.displayOrder), 0) FROM Photo p WHERE p.albumId = :albumId")
    int findMaxDisplayOrderByAlbumId(@Param("albumId") Long albumId);

}
