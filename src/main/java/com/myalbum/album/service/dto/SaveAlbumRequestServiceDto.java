package com.myalbum.album.service.dto;

import com.myalbum.album.entity.Album;
import com.myalbum.album.enums.AlbumStatus;
import com.myalbum.common.storage.entity.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SaveAlbumRequestServiceDto {

    private String title;

    private String description;

    private UploadFile uploadFile;

    private AlbumStatus status;

    private Long memberId;

    public static Album toAlbumEntity(SaveAlbumRequestServiceDto saveAlbumRequestServiceDto) {
        return Album.builder()
                .title(saveAlbumRequestServiceDto.getTitle())
                .description(saveAlbumRequestServiceDto.getDescription())
                .uploadFile(saveAlbumRequestServiceDto.getUploadFile())
                .status(AlbumStatus.PUBLIC)
                .memberId(saveAlbumRequestServiceDto.getMemberId())
                .build();
    }

}
