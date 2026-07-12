package com.myalbum.photo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AlbumOwnerResponse {

    private String username;

    private String albumId;

    private String albumName;

}
