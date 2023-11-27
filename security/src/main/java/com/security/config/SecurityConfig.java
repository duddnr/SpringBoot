package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.formLogin(formLogin ->formLogin
				.loginPage("/login") // 로그인 페이지 설정
				.defaultSuccessUrl("/main") // 로그인 성공 페이지 설정
				.usernameParameter("userId") // 로그인 시 사용할 파라미터 설정
				.failureUrl("/login/error")) // 로그인 실패 페이지 설정
		.logout(logout->logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 페이지 설정
				.logoutSuccessUrl("/main")); // 로그아웃 성공 페이지 설정
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(); // 비밀번호를 암호화 하여 데이터베이스에 저장
	}
}