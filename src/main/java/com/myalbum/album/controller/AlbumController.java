package com.myalbum.album.controller;

import com.myalbum.album.controller.dto.SaveAlbumRequest;
import com.myalbum.album.service.AlbumService;
import com.myalbum.album.service.dto.AlbumListResponse;
import com.myalbum.album.service.dto.SaveAlbumResponse;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * 사용자 앨범 저장
     *
     * @param saveAlbumRequest 앨범 저장 요청 정보
     * @param file             업로드할 파일
     * @param principalDetails 인증된 사용자 정보
     * @return 저장된 앨범 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SaveAlbumResponse>> saveAlbum(
            @RequestPart(name = "saveAlbumRequest") SaveAlbumRequest saveAlbumRequest,
            @RequestPart(name = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getId();
        // 사용자 앨범 저장
        SaveAlbumResponse saveAlbumResponse = albumService.saveAlbum(saveAlbumRequest, file, memberId);

        return ApiResponse.ok(saveAlbumResponse);
    }

}
