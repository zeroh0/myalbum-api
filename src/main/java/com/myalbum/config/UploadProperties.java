package com.myalbum.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UploadProperties {

    @Value("${file.storage.location}")
    private String uploadPath;

}
