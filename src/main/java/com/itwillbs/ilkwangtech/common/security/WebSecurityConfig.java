package com.itwillbs.ilkwangtech.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		// HttpSecurity 객체의 다양한 메서드를 메서트 체이닝 형태로 호출하여 스프링 시큐리티 관련 설정을 수행하고
		// 마지막에 build() 메서드를 호출하여 HttpSecurity 객체를 생성 후 외부로 리턴(= 빌더 패턴)
		return httpSecurity
				// 요청에 대한 접근 허용 여부 등의 권한 설정 생략 (접속시 무조건 login 페이지로 가도록 설정)
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						// 로그인 페이지에는 모든 사용자가 접속 가능
						.requestMatchers("/account/login").permitAll()
						// 로그인 페이지 외에 모든 요청은 인증된 사용자만 가능
						.anyRequest().authenticated()
				)
				// 로그인 처리 설정
				.formLogin(formLogin -> formLogin
						// 스프링 시큐리티에서 로그인 요청을 위한 폼 요청 주소
						.loginPage("/account/login") 
						 // 로그인 폼에서 제풀한 데이터 처리(로그인 처리)용 요청 주소
						.loginProcessingUrl("/acccount/login")
						// 로그인 과정에서 로그인에 사용되는 UserDetailsService 객체의 loadByUsername() 메서드가 자동 호출됨
						// 로그인에 사용할 아이디는 사원번호로 설정
						.usernameParameter("employeeNumber")
						// 로그인에 사용할 패드워드 이름 지정(기본값 : password)
//						.passwordParameter("password") // 기본값이 password 이므로 생략
						.defaultSuccessUrl("/layout/layout", true) // 로그인 성공 시 리디렉션 URL 설정
						.permitAll() // 로그인 관련 요청 주소를 모두 허용 경로로 등록
				)
				// 로그아웃 처리 설정
				.logout(logoutCustomizer -> logoutCustomizer
						.logoutUrl("/accounts/logout")
						.logoutSuccessUrl("/")
						.permitAll()
				)
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
