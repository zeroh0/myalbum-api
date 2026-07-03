package com.myalbum.album.service.dto;

import com.myalbum.album.entity.Album;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SaveAlbumRequestServiceDto {

    private String title;

    private String description;

    private String coverImageUrl;

    private String status;

    private Long memberId;

    public static Album toAlbumEntity(SaveAlbumRequestServiceDto saveAlbumRequestServiceDto) {
        return Album.builder()
                .title(saveAlbumRequestServiceDto.getTitle())
                .description(saveAlbumRequestServiceDto.getDescription())
                .coverImageUrl(saveAlbumRequestServiceDto.getCoverImageUrl())
                .status("PUBLIC")
                .memberId(saveAlbumRequestServiceDto.getMemberId())
                .build();
    }

}
