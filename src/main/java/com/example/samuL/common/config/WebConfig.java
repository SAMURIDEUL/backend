package com.example.samuL.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads/review_images}")
    private String uploadDir;

    @Value("${file.access-url-prefix:/uploads/review_images/}")
    private String accessUrlPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ensure prefix ends with a slash
        if (!accessUrlPrefix.endsWith("/")) {
            accessUrlPrefix += "/";
        }
        
        // e.g., "/uploads/review_images/**"
        String urlPattern = accessUrlPrefix + "**";
        
        // Ensure uploadDir is absolute
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String resourceLocation = "file:" + uploadPath.toString() + "/";

        registry.addResourceHandler(urlPattern)
                .addResourceLocations(resourceLocation);
    }
}
