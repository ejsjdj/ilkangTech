package com.itwillbs.ilkwangtech;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan({
        "com.itwillbs.ilkwangtech.sales.mapper",
        "com.itwillbs.ilkwangtech.item.mapper",
        "com.itwillbs.ilkwangtech.standard.mapper"
})
public class IlkwangTechApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IlkwangTechApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(IlkwangTechApplication.class, args);
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌스프링프로젝트 실행🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
    }
}
