package com.myalbum.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Profile("local")
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UploadProperties uploadProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + Path.of(uploadProperties.getUploadPath()).toAbsolutePath().toString();

        registry.addResourceHandler("/resource/**")
                .addResourceLocations(location);
    }

}
