package com.myalbum.album.controller;

import com.myalbum.album.service.AlbumService;
import com.myalbum.album.service.dto.AlbumListResponse;
import com.myalbum.common.response.ApiResponse;
import com.myalbum.config.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
