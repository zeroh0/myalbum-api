package com.myalbum.album.service.dto;

import com.myalbum.album.entity.Album;
import com.myalbum.album.enums.AlbumStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AlbumListResponse {

    private Long id;

    private String title;

    private Long imageId;

    private int viewCount;

    private AlbumStatus status;

    private LocalDateTime createdAt;

    public static List<AlbumListResponse> fromAlbumEntity(List<Album> albums) {
        return albums.stream()
                .map(album -> {
                    AlbumListResponse response = new AlbumListResponse();
                    response.id = album.getId();
                    response.title = album.getTitle();
                    response.imageId = album.getImageId();
                    response.viewCount = album.getViewCount();
                    response.status = album.getStatus();
                    response.createdAt = album.getCreatedAt();
                    return response;
                })
                .toList();
    }

}
