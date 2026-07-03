package com.myalbum.album.service;

import com.myalbum.album.controller.dto.SaveAlbumRequest;
import com.myalbum.album.entity.Album;
import com.myalbum.album.exception.AlbumError;
import com.myalbum.album.repository.AlbumRepository;
import com.myalbum.album.service.dto.AlbumListResponse;
import com.myalbum.album.service.dto.SaveAlbumRequestServiceDto;
import com.myalbum.album.service.dto.SaveAlbumResponse;
import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AlbumService {

    private final FileStorage fileStorage;
    private final AlbumRepository albumRepository;

    /**
     * 사용자 앨범 목록 조회
     *
     * @param memberId 사용자 ID
     * @return 사용자 앨범 목록
     */
    @Transactional(readOnly = true)
    public List<AlbumListResponse> getAlbumList(Long memberId) {
        List<Album> albums = albumRepository.findByMemberId(memberId).orElse(Collections.emptyList());

        return AlbumListResponse.fromAlbumEntity(albums);
    }

    /**
     * 사용자 앨범 저장
     *
     * @param saveAlbumRequest 사용자 앨범 저장 요청
     * @return 저장된 앨범 정보
     */
    public SaveAlbumResponse saveAlbum(SaveAlbumRequest saveAlbumRequest, MultipartFile file, Long memberId) {
        // 물리적 저장
        String storedFileUrl = fileStorage.storeFile(file);

        try {
            // 논리적 저장
            SaveAlbumRequestServiceDto saveAlbumRequestServiceDto = SaveAlbumRequestServiceDto.builder()
                    .title(saveAlbumRequest.getTitle())
                    .description(saveAlbumRequest.getDescription())
                    .coverImageUrl(storedFileUrl)
                    .memberId(memberId)
                    .build();

            Album album = SaveAlbumRequestServiceDto.toAlbumEntity(saveAlbumRequestServiceDto);
            Album savedAlbum = albumRepository.save(album);

            return SaveAlbumResponse.fromAlbumEntity(savedAlbum);
        } catch (Exception exception) {
            // 저장 실패 시 파일 삭제
            fileStorage.delete(storedFileUrl);
            throw exception;
        }
    }

    /**
     * 사용자 앨범 삭제
     *
     * @param albumId  앨범 ID
     * @param memberId 사용자 ID
     */
    public void deleteAlbum(Long albumId, Long memberId) {
        Album album = albumRepository.findByIdAndMemberId(albumId, memberId)
                .orElseThrow(() -> AppException.exception(AlbumError.ALBUM_NOT_FOUND));

        album.delete();
    }

}
