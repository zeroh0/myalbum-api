package com.myalbum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Profile("local")
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.storage.location}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + Path.of(uploadPath).toAbsolutePath().toString();

        registry.addResourceHandler("/resource/**")
                .addResourceLocations(location);
    }

}
