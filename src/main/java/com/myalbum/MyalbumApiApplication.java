package com.myalbum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class MyalbumApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyalbumApiApplication.class, args);
    }

}
