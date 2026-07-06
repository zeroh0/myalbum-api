package com.myalbum.photo.service;

import com.myalbum.album.entity.Album;
import com.myalbum.album.exception.AlbumError;
import com.myalbum.album.repository.AlbumRepository;
import com.myalbum.common.error.exception.AppException;
import com.myalbum.common.storage.entity.UploadFile;
import com.myalbum.common.storage.repository.UploadRepository;
import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.member.entity.Member;
import com.myalbum.photo.entity.Photo;
import com.myalbum.photo.repository.PhotoRepository;
import com.myalbum.photo.service.dto.SavePhotoRequest;
import com.myalbum.photo.service.dto.UploadPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final UploadRepository uploadRepository;

    /**
     * 사진 저장
     *
     * @param albumId          앨범 ID
     * @param savePhotoRequest 저장할 사진 정보
     */
    public void savePhoto(Long albumId, SavePhotoRequest savePhotoRequest) {
        // 로그인한 사용자 정보 가져오기
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = principal.getMember();

        // 앨범 조회 및 권한 확인
        Album album = albumRepository.findByIdAndMemberId(albumId, member.getId())
                .orElseThrow(() -> AppException.exception(AlbumError.ALBUM_NOT_FOUND));
        Long targetAlbumId = album.getId();

        // 이미지 ID 검증
        AtomicInteger displayOrder = new AtomicInteger(1);
        List<UploadPhoto> uploadPhotoList = savePhotoRequest.getUploadPhotoList();
        uploadPhotoList.forEach(uploadPhoto -> {

            // 썸네일 이미지 ID와 이미지 ID가 존재하는지 확인
            UploadFile thumbnailUploadFile = uploadRepository.findById(uploadPhoto.getThumbnailImageId())
                    .orElseThrow(() -> AppException.exception(AlbumError.UPLOAD_FILE_NOT_FOUND));

            UploadFile imageUploadFile = uploadRepository.findById(uploadPhoto.getImageId())
                    .orElseThrow(() -> AppException.exception(AlbumError.UPLOAD_FILE_NOT_FOUND));

            // 파일 상태 값 변경 (CONFIRMED)
            thumbnailUploadFile.confirmed();
            imageUploadFile.confirmed();

            Photo thumbnailPhoto = Photo.builder()
                    .albumId(targetAlbumId)
                    .displayOrder(displayOrder.get())
                    .uploadFile(thumbnailUploadFile)
                    .build();

            Photo imagePhoto = Photo.builder()
                    .albumId(targetAlbumId)
                    .displayOrder(displayOrder.getAndIncrement())
                    .uploadFile(imageUploadFile)
                    .build();

            photoRepository.save(thumbnailPhoto);
            photoRepository.save(imagePhoto);
        });
    }

}
