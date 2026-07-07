package com.myalbum.album.controller;

import com.myalbum.album.controller.dto.SaveAlbumRequest;
import com.myalbum.album.service.AlbumService;
import com.myalbum.album.service.dto.AlbumListResponse;
import com.myalbum.album.service.dto.AlbumResponse;
import com.myalbum.album.service.dto.SaveAlbumResponse;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    /**
     * 사용자 앨범 목록 조회
     *
     * @return 사용자 앨범 목록
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AlbumListResponse>>> getAlbumList(
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        // 사용자 앨범 목록 조회
        List<AlbumListResponse> albumList = albumService.getAlbumList(principalDetails.getMember().getId());

        return ApiResponse.ok(albumList);
    }

    /**
     * 사용자핸들 기준 앨범 목록 조회 (공개, 인증 불필요)
     * 본인이 조회하는 경우에만 비공개 앨범도 함께 조회된다.
     *
     * @param username         사용자핸들
     * @param principalDetails 인증된 사용자 정보 (없을 수 있음)
     * @return 앨범 목록
     */
    @GetMapping("/list/{username}")
    public ResponseEntity<ApiResponse<List<AlbumListResponse>>> getAlbumListByUsername(
            @PathVariable String username,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        boolean isOwner = principalDetails != null
                && username.equals(principalDetails.getMember().getUsername());

        List<AlbumListResponse> albumList = albumService.getAlbumListByUsername(username, isOwner);

        return ApiResponse.ok(albumList);
    }

    /**
     * 사용자 앨범 저장
     *
     * @param saveAlbumRequest 앨범 저장 요청 정보
     * @param principalDetails 인증된 사용자 정보
     * @return 저장된 앨범 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SaveAlbumResponse>> saveAlbum(
            @RequestBody SaveAlbumRequest saveAlbumRequest,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getId();
        // 사용자 앨범 저장
        SaveAlbumResponse saveAlbumResponse = albumService.saveAlbum(saveAlbumRequest, memberId);

        return ApiResponse.ok(saveAlbumResponse);
    }

    /**
     * 사용자 앨범 삭제
     *
     * @param albumId          앨범 ID
     * @param principalDetails 인증된 사용자 정보
     * @return 삭제 결과
     */
    @DeleteMapping("/{albumId}")
    public ResponseEntity<ApiResponse<Void>> deleteAlbum(
            @PathVariable Long albumId,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getId();
        // 사용자 앨범 삭제
        albumService.deleteAlbum(albumId, memberId);

        return ApiResponse.ok(null);
    }

    /**
     * 사용자 앨범 수정
     *
     * @param albumId          앨범 ID
     * @param saveAlbumRequest 앨범 수정 요청 정보
     * @param principalDetails 인증된 사용자 정보
     * @return 수정된 앨범 정보
     */
    @PutMapping("/{albumId}")
    public ResponseEntity<ApiResponse<SaveAlbumResponse>> updateAlbum(
            @PathVariable Long albumId,
            @RequestBody SaveAlbumRequest saveAlbumRequest,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getId();
        // 사용자 앨범 수정
        SaveAlbumResponse saveAlbumResponse = albumService.updateAlbum(albumId, saveAlbumRequest, memberId);

        return ApiResponse.ok(saveAlbumResponse);
    }

    /**
     * 사용자 앨범 상세 조회
     *
     * @param albumId          앨범 ID
     * @param principalDetails 인증된 사용자 정보
     * @return 앨범 상세 정보
     */
    @GetMapping("/{albumId}")
    public ResponseEntity<ApiResponse<AlbumResponse>> getAlbum(
            @PathVariable Long albumId,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getId();
        // 사용자 앨범 조회
        AlbumResponse albumResponse = albumService.getAlbum(albumId, memberId);

        return ApiResponse.ok(albumResponse);
    }

}
