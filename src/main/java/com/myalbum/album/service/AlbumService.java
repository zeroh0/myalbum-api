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
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AlbumService {

    private final FileStorage fileStorage;
    private final UploadRepository uploadRepository;
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
     * @param memberId         사용자 ID
     * @return 저장된 앨범 정보
     */
    public SaveAlbumResponse saveAlbum(SaveAlbumRequest saveAlbumRequest, Long memberId) {
        // 업로드된 파일 가져오기
        Long imageId;
        imageId = saveAlbumRequest.getSaveUploadFileRequest().getId();
        if (imageId != null) {
            UploadFile uploadFile = uploadRepository.findById(imageId)
                    .orElseThrow(() -> AppException.exception(AlbumError.UPLOAD_FILE_NOT_FOUND));

            // 파일 상태 값 변경 (CONFIRMED)
            uploadFile.confirmed();
        }

        // 앨범 저장
        SaveAlbumRequestServiceDto saveAlbumRequestServiceDto = SaveAlbumRequestServiceDto.builder()
                .title(saveAlbumRequest.getTitle())
                .description(saveAlbumRequest.getDescription())
                .imageId(imageId)
                .memberId(memberId)
                .build();

        Album album = SaveAlbumRequestServiceDto.toAlbumEntity(saveAlbumRequestServiceDto);
        Album savedAlbum = albumRepository.save(album);

        return SaveAlbumResponse.fromAlbumEntity(savedAlbum);
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

    /**
     * 사용자 앨범 수정
     *
     * @param albumId          앨범 ID
     * @param saveAlbumRequest 앨범 수정 요청 정보
     * @param memberId         사용자 ID
     * @return 수정된 앨범 정보
     */
    public SaveAlbumResponse updateAlbum(Long albumId, SaveAlbumRequest saveAlbumRequest, Long memberId) {
        // 앨범 조회
        Album album = albumRepository.findByIdAndMemberId(albumId, memberId)
                .orElseThrow(() -> AppException.exception(AlbumError.ALBUM_NOT_FOUND));

        // 수정 업로드 파일 조회
        Long changedImageId;
        changedImageId = saveAlbumRequest.getSaveUploadFileRequest().getId();
        if (changedImageId != null) {
            UploadFile uploadFile = uploadRepository.findById(changedImageId)
                    .orElseThrow(() -> AppException.exception(AlbumError.UPLOAD_FILE_NOT_FOUND));

            // 파일 상태 값 변경 (CONFIRMED)
            uploadFile.confirmed();
        }

        // 앨범 수정
        album.update(saveAlbumRequest.getTitle(), saveAlbumRequest.getDescription(), changedImageId);

        return SaveAlbumResponse.fromAlbumEntity(album);
    }

}
