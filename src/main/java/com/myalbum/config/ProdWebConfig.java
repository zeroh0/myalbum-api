package com.myalbum.config;

import com.myalbum.common.storage.FileStorageProperties;
import com.myalbum.common.storage.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("prod")
@Configuration
@RequiredArgsConstructor
public class ProdWebConfig implements WebMvcConfigurer {

    private final S3Properties s3Properties;
    private final FileStorageProperties fileStorageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = s3Properties.getPublicBaseUrl() + "/" + fileStorageProperties.getLocation() + "/";

        registry.addResourceHandler("/resource/**")
                .addResourceLocations(location);
    }

}
