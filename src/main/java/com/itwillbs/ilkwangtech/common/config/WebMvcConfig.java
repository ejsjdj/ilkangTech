package com.itwillbs.ilkwangtech.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value("${file.uploadBaseLocation}")
    private String uploadBaseLocation;

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹 브라우저에서 /upload/** 로 들어오는 요청을
        // 실제 내 컴퓨터 C:/upload/ 폴더로 연결해준다.
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/upload/");
    }

}
