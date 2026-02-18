package com.itwillbs.ilkwangtech.config;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 중요: XML 매퍼 파일의 위치를 알려줘야 합니다!
        // classpath 뒤의 경로를 폴더 구조에 맞게 수정하세요.
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/salesMapper/*.xml"));
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/itemMapper/*.xml"));

        return factoryBean.getObject();
    }
}

