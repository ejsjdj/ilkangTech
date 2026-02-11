package com.itwillbs.ilkwangtech;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan({
        "com.itwillbs.ilkwangtech.sales.mapper",
        "com.itwillbs.ilkwangtech.item.mapper"
})
public class IlkwangTechApplication {
    public static void main(String[] args) {
        SpringApplication.run(IlkwangTechApplication.class, args);
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌스프링프로젝트 실행🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
        System.out.println("🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌🙌");
    }
}
