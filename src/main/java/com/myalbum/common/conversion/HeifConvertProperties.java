package com.myalbum.common.conversion;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class HeifConvertProperties {

    @Value("${heif.convert.path}")
    private String path;

    @Value("${heif.convert.temp-dir}")
    private String tempDirPath;

}
