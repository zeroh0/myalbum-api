package com.myalbum.photo.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReorderPhotosRequest {

    private Long albumId;

    private List<Long> photoIds;

}
