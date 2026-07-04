package com.myalbum.album.service.dto;

import com.myalbum.album.entity.Album;
import com.myalbum.album.enums.AlbumStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SaveAlbumRequestServiceDto {

    private String title;

    private String description;

    private Long imageId;

    private AlbumStatus status;

    private Long memberId;

    public static Album toAlbumEntity(SaveAlbumRequestServiceDto saveAlbumRequestServiceDto) {
        return Album.builder()
                .title(saveAlbumRequestServiceDto.getTitle())
                .description(saveAlbumRequestServiceDto.getDescription())
                .imageId(saveAlbumRequestServiceDto.getImageId())
                .status(AlbumStatus.PUBLIC)
                .memberId(saveAlbumRequestServiceDto.getMemberId())
                .build();
    }

}
