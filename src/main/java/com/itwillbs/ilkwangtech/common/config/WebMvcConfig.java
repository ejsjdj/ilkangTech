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
        // 프로필 이미지 요청 (/images/profileImg/**)은 로컬 디스크의 uploadBaseLocation 폴더에서 파일을 찾는다.
        registry.addResourceHandler("/images/profileImg/**")
                .addResourceLocations("file:/C:/" + uploadBaseLocation + "/");
    }

}
