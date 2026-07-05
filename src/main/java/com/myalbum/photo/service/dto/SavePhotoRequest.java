package com.myalbum.photo.service.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SavePhotoRequest {

    private List<UploadPhoto> uploadPhotoList;

}
