package com.itwillbs.ilkwangtech.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {

		http
				.authorizeHttpRequests((auth) -> auth
						.requestMatchers("/", "/login").permitAll()
//						.requestMatchers("/admin").hasRole("ADMIN")
//						.requestMatchers("/my/**").hasAnyRole("ADMIN","USER")
						.anyRequest().permitAll()
				);

		http
				.formLogin(login -> login
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.usernameParameter("employeeNumber")
						.passwordParameter("password")
						.defaultSuccessUrl("/schedule/calender")
						.failureHandler(new CustomAuthenticationFailureHandler())
						.successHandler(new CustomAuthenticationSuccessHandler())
						.permitAll()
				);

		http
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login")
						.permitAll()
				);

		http
				.rememberMe(rem -> rem
						.rememberMeParameter("remember-me")
						.key("key")
						.tokenValiditySeconds(60 * 60 * 24 * 7)
				);

		http
				.csrf(csrf -> csrf.disable());

		return http.build();
	}
}
