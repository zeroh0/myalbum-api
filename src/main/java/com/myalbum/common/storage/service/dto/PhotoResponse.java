package com.myalbum.common.storage.service.dto;

import com.myalbum.common.storage.entity.UploadFile;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PhotoResponse {

    private Long id;

    private String url;

    private String originFileName;

    private String saveFileName;

    private Long size;

    private Integer width;

    private Integer height;

    private String cameraModel;

    private String lensModel;

    private Integer iso;

    private String aperture;

    private String shutterSpeed;

    private LocalDate takenAt;

    public static PhotoResponse fromUploadFileEntity(UploadFile uploadFile) {
        PhotoResponse response = new PhotoResponse();
        response.id = uploadFile.getId();
        response.url = uploadFile.getUrl();
        response.originFileName = uploadFile.getOriginFileName();
        response.saveFileName = uploadFile.getSaveFileName();
        response.size = uploadFile.getSize();
        response.width = uploadFile.getWidth();
        response.height = uploadFile.getHeight();
        response.cameraModel = uploadFile.getCameraModel();
        response.lensModel = uploadFile.getLensModel();
        response.iso = uploadFile.getIso();
        response.aperture = uploadFile.getAperture();
        response.shutterSpeed = uploadFile.getShutterSpeed();
        response.takenAt = uploadFile.getTakenAt();
        return response;
    }

}
