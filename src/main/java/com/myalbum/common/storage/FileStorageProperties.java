package com.myalbum.common.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

    private String location;

    private Long maxFileSize;

}
