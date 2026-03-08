package com.itwillbs.ilkwangtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class IlkwangTechApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(IlkwangTechApplication.class, args);
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌스프링프로젝트 실행🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
    }
}