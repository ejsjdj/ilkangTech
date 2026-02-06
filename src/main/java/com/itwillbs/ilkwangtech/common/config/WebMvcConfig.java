package com.itwillbs.ilkwangtech.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * 프로필 이미지 요청을 처리하는 컨트롤러
 * WebMvcConfigurer 의 addResourceHandlers 는
 * URL 경로 -> "실제 파일 경로"로 매핑한다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${file.uploadBaseLocation}")
    private String uploadBaseLocation;

    @Value("${file.profileImgLocation}")
    private String profileImgLocation;

    /**
     * OS 에 종속되지 않고 어떤 OS 에서든
     * 파일 경로를 동적으로 생성하여 매핑하기 위해
     * Path 로 경로를 생성한다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 이미지를 불러오기 위한 경로 생성
        String profileImgPath = Paths.get(uploadBaseLocation, profileImgLocation)
                                     .toAbsolutePath()
                                     .normalize()
                                     .toString();

        // "/images/profileImg/**" 경로로 요청이 들어오면 위에서 생성된 절대 경로로 매핑
        registry.addResourceHandler("/images/profileImg/**")
                .addResourceLocations(profileImgPath);
    }

}