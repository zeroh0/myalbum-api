package com.myalbum.photo.service;

import com.myalbum.album.entity.Album;
import com.myalbum.album.enums.AlbumStatus;
import com.myalbum.album.exception.AlbumError;
import com.myalbum.album.repository.AlbumRepository;
import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.repository.UploadRepository;
import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.member.entity.Member;
import com.myalbum.photo.entity.Photo;
import com.myalbum.photo.repository.PhotoRepository;
import com.myalbum.photo.service.dto.AlbumPhotoListResponse;
import com.myalbum.photo.service.dto.PhotoListResponse;
import com.myalbum.photo.service.dto.SavePhotoRequest;
import com.myalbum.photo.service.dto.UploadPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final UploadRepository uploadRepository;

    /**
     * 앨범과 사진 목록 조회 (공개 프로필)
     * PUBLIC 앨범은 누구나, PRIVATE 앨범은 소유자만 조회 가능
     *
     * @param albumId        앨범 ID
     * @param viewerMemberId 조회하는 회원 ID (비로그인 시 null)
     * @return 앨범 정보와 사진 목록
     */
    @Transactional(readOnly = true)
    public AlbumPhotoListResponse getPhotoList(Long albumId, Long viewerMemberId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> AppException.exception(AlbumError.ALBUM_NOT_FOUND));

        // 소유자 여부 확인
        boolean isOwner = album.getMemberId().equals(viewerMemberId);
        if (album.getStatus() != AlbumStatus.PUBLIC && !isOwner) {
            throw AppException.exception(AlbumError.ALBUM_NOT_FOUND);
        }

        List<Photo> photoList = photoRepository.findByAlbumIdOrderByDisplayOrderAsc(albumId);

        return AlbumPhotoListResponse.builder()
                .albumId(album.getId())
                .title(album.getTitle())
                .description(album.getDescription())
                .status(album.getStatus().name())
                .owner(isOwner)
                .photos(PhotoListResponse.fromPhotoEntities(photoList))
                .build();
    }

    /**
     * 사진 저장 (기존 사진 뒤에 이어서 순서를 매긴다)
     *
     * @param albumId          앨범 ID
     * @param savePhotoRequest 저장할 사진 정보 (전달된 순서가 곧 표시 순서)
     */
    public void savePhoto(Long albumId, SavePhotoRequest savePhotoRequest) {
        // 로그인한 사용자 정보 가져오기
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = principal.getMember();

        // 앨범 조회 및 권한 확인
        Album album = albumRepository.findByIdAndMemberId(albumId, member.getId())
                .orElseThrow(() -> AppException.exception(AlbumError.ALBUM_NOT_FOUND));
        Long targetAlbumId = album.getId();

        // 기존 사진 중 가장 큰 순서 다음부터 이어서 매긴다
        int nextOrder = photoRepository.findMaxDisplayOrderByAlbumId(targetAlbumId) + 1;

        List<UploadPhoto> uploadPhotoList = savePhotoRequest.getUploadPhotoList();
        for (UploadPhoto uploadPhoto : uploadPhotoList) {
            // 썸네일 이미지 ID와 이미지 ID가 존재하는지 확인
            UploadFile thumbnailUploadFile = uploadRepository.findById(uploadPhoto.getThumbnailImageId())
                    .orElseThrow(() -> AppException.exception(AlbumError.UPLOAD_FILE_NOT_FOUND));

            UploadFile originUploadFile = uploadRepository.findById(uploadPhoto.getImageId())
                    .orElseThrow(() -> AppException.exception(AlbumError.UPLOAD_FILE_NOT_FOUND));

            // 파일 상태 값 변경 (CONFIRMED)
            thumbnailUploadFile.confirmed();
            originUploadFile.confirmed();

            Photo photo = Photo.builder()
                    .albumId(targetAlbumId)
                    .displayOrder(nextOrder++)
                    .thumbnailUploadFile(thumbnailUploadFile)
                    .originUploadFile(originUploadFile)
                    .build();

            photoRepository.save(photo);
        }
    }

    /**
     * 사진 삭제
     *
     * @param photoId  사진 ID
     * @param memberId 사용자 ID
     */
    public void deletePhoto(Long photoId, Long memberId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> AppException.exception(AlbumError.PHOTO_NOT_FOUND));

        Album album = albumRepository.findByIdAndMemberId(photo.getAlbumId(), memberId)
                .orElseThrow(() -> AppException.exception(AlbumError.ALBUM_NOT_FOUND));

        if (!album.getMemberId().equals(memberId)) {
            throw AppException.exception(AlbumError.ALBUM_NOT_FOUND);
        }

        photoRepository.delete(photo);
    }

    /**
     * 사진 순서 변경
     *
     * @param albumId         앨범 ID
     * @param orderedPhotoIds 새로운 순서대로 나열된 사진 ID 목록
     * @param memberId        사용자 ID
     */
    public void reorderPhotos(Long albumId, List<Long> orderedPhotoIds, Long memberId) {
        Album album = albumRepository.findByIdAndMemberId(albumId, memberId)
                .orElseThrow(() -> AppException.exception(AlbumError.ALBUM_NOT_FOUND));

        List<Photo> photoList = photoRepository.findByAlbumIdOrderByDisplayOrderAsc(album.getId());
        Map<Long, Photo> photoMap = new HashMap<>();
        photoList.forEach(photo -> photoMap.put(photo.getId(), photo));

        for (int i = 0; i < orderedPhotoIds.size(); i++) {
            Photo photo = photoMap.get(orderedPhotoIds.get(i));
            if (photo == null) {
                throw AppException.exception(AlbumError.PHOTO_NOT_FOUND);
            }
            photo.updateDisplayOrder(i + 1);
        }
    }

}
