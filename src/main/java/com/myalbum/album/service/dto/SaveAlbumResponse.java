package com.myalbum.album.service.dto;

import com.myalbum.album.entity.Album;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SaveAlbumResponse {

    private Long id;

    public static SaveAlbumResponse fromAlbumEntity(Album savedAlbum) {
        return SaveAlbumResponse.builder()
                .id(savedAlbum.getId())
                .build();
    }

}
