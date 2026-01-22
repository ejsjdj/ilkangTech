package com.itwillbs.ilkwangtech.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value("${file.upload.path:C:/upload/}") // application.properties 경로
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // [설명] /upload/로 시작하는 URL 요청은 로컬 디스크의 uploadPath 폴더에서 파일을 찾는다.
        // 윈도우의 경우 file:/// 접두어가 필수입니다.
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + uploadPath);
    }

}
