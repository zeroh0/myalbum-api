package com.myalbum.photo.controller;

import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.security.domain.PrincipalDetails;
import com.myalbum.photo.controller.dto.ReorderPhotosRequest;
import com.myalbum.photo.service.PhotoService;
import com.myalbum.photo.service.dto.AlbumPhotoListResponse;
import com.myalbum.photo.service.dto.SavePhotoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    /**
     * 앨범과 사진 목록 조회 (공개 프로필, 인증 불필요)
     * PRIVATE 앨범은 본인일 때만 조회된다.
     *
     * @param albumId          앨범 ID
     * @param principalDetails 인증된 사용자 정보 (없을 수 있음)
     * @return 앨범 정보와 사진 목록
     */
    @GetMapping("/{albumId}")
    public ResponseEntity<ApiResponse<AlbumPhotoListResponse>> getPhotoList(
            @PathVariable Long albumId,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long viewerMemberId = principalDetails != null ? principalDetails.getMember().getId() : null;
        AlbumPhotoListResponse response = photoService.getPhotoList(albumId, viewerMemberId);

        return ApiResponse.ok(response);
    }

    /**
     * 사진 저장
     *
     * @param albumId          앨범 ID
     * @param savePhotoRequest 저장할 사진 정보
     */
    @PostMapping("/{albumId}")
    public ResponseEntity<ApiResponse<Void>> savePhoto(
            @PathVariable Long albumId,
            @RequestBody SavePhotoRequest savePhotoRequest
    ) {
        photoService.savePhoto(albumId, savePhotoRequest);

        return ApiResponse.ok();
    }

    /**
     * 사진 삭제
     *
     * @param photoId          사진 ID
     * @param principalDetails 인증된 사용자 정보
     */
    @DeleteMapping("/{photoId}")
    public ResponseEntity<ApiResponse<Void>> deletePhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getId();

        // 사진 삭제
        photoService.deletePhoto(photoId, memberId);

        return ApiResponse.ok();
    }

    /**
     * 사진 순서 변경
     *
     * @param request          변경할 앨범 ID와 새로운 순서의 사진 ID 목록
     * @param principalDetails 인증된 사용자 정보
     */
    @PatchMapping("/reorder")
    public ResponseEntity<ApiResponse<Void>> reorderPhotos(
            @RequestBody ReorderPhotosRequest request,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long albumId = request.getAlbumId();
        // 새로운 순서대로 나열된 사진 ID 목록
        List<Long> orderedPhotoIds = request.getPhotoIds();
        Long memberId = principalDetails.getMember().getId();

        // 사진 순서 변경
        photoService.reorderPhotos(albumId, orderedPhotoIds, memberId);

        return ApiResponse.ok();
    }

}
