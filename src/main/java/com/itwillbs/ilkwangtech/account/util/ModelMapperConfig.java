package com.itwillbs.ilkwangtech.account.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Member -> AccountLogin 매핑 시 department, position, bank 필드 무시
        // (CustomUserDetailsService 에서 직접 부서명/직급명/은행명으로 변환하여 설정하기 때문)
        modelMapper.typeMap(com.itwillbs.ilkwangtech.member.entity.Member.class, com.itwillbs.ilkwangtech.account.dto.AccountLogin.class)
            .addMappings(mapper -> {
                mapper.skip(com.itwillbs.ilkwangtech.account.dto.AccountLogin::setDepartment);
                mapper.skip(com.itwillbs.ilkwangtech.account.dto.AccountLogin::setPosition);
                mapper.skip(com.itwillbs.ilkwangtech.account.dto.AccountLogin::setBank);
            });
            
        return modelMapper;
    }


}
