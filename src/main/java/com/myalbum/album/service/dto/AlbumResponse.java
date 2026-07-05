package com.myalbum.album.service.dto;

import com.myalbum.album.entity.Album;
import com.myalbum.common.storage.entity.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AlbumResponse {

    private Long id;

    private String title;

    private String description;

    private UploadFile uploadFile;

    public static AlbumResponse fromAlbumEntity(Album album) {
        return AlbumResponse.builder()
                .id(album.getId())
                .title(album.getTitle())
                .description(album.getDescription())
                .uploadFile(album.getUploadFile())
                .build();
    }

}
