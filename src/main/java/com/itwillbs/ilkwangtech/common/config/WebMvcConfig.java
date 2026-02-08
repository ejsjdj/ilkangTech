package com.itwillbs.ilkwangtech.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.uploadBaseLocation}")
    private String baseDir; // /usr/local/tomcat/upload/

    @Value("${file.profileImgLocation}")
    private String profileImgLocation; // images/profileImg

    private String getRealUploadPath() {
        String path = baseDir;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (path.startsWith("/")) {
                path = "C:" + path;
            }
        }
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String realPath = getRealUploadPath();

        String profileImgPath = Paths.get(realPath, profileImgLocation)
                                     .toAbsolutePath()
                                     .normalize()
                                     .toString();

        registry.addResourceHandler("/images/profileImg/**")
                .addResourceLocations("file:///" + profileImgPath + "/");
        
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + realPath);
    }
}