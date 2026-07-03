package com.myalbum.album.service;

import com.myalbum.album.entity.Album;
import com.myalbum.album.repository.AlbumRepository;
import com.myalbum.album.service.dto.AlbumListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    /**
     * 사용자 앨범 목록 조회
     *
     * @param memberId 사용자 ID
     * @return 사용자 앨범 목록
     */
    public List<AlbumListResponse> getAlbumList(Long memberId) {
        List<Album> albums = albumRepository.findByMemberId(memberId).orElse(Collections.emptyList());

        return AlbumListResponse.fromAlbumEntity(albums);
    }

}
