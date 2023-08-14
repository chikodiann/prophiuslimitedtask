package com.ann.prophiuslimitedtask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = "uploads";
        Path uploadPath = Paths.get(uploadDir);
        String uploadAbsolutePath = uploadPath.toFile().getAbsolutePath();
        // Configure resource handlers for uploaded files
        registry.addResourceHandler( "/" + uploadDir + "/**")
                .addResourceLocations("file:" + uploadAbsolutePath + "/");
    }
}

/*This configuration class is responsible for handling resource paths in the application.
It sets up resource handlers for uploaded files, allowing them to be served directly from the file system.
This is useful for displaying or downloading files uploaded by users.
 */