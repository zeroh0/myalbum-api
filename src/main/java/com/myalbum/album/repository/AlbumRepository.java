package com.myalbum.album.repository;

import com.myalbum.album.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    /**
     * 사용자 앨범 목록 조회
     *
     * @param memberId 사용자 ID
     * @return 사용자 앨범 목록
     */
    Optional<List<Album>> findByMemberId(Long memberId);

}
