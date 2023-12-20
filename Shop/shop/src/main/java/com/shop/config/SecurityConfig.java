package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.shop.service.MemberService;

@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
	@Autowired
	MemberService memberService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception // http 요청에 대한 보안 설정 메소드
	{
		http
		.formLogin(formLogin -> 
					formLogin.loginPage("/members/login") // 로그인 페이지 URL 설정
					.defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL 페이지 설정
					.usernameParameter("email") // 로그인 시 사용할 파라미터 이름 지정
					.failureUrl("/members/login/error")) // 로그인 실패 시 이동할 URL 설정
		.logout(logout -> 
					logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 페이지 URL 설정
					.logoutSuccessUrl("/")); // 로그아웃 성공 시 이동할 URL 페이지 설정
		
		http.authorizeHttpRequests(authorize ->
					authorize.requestMatchers(
							new AntPathRequestMatcher("/"),
							new AntPathRequestMatcher("/members/**"), 
							new AntPathRequestMatcher("/item/**"), 
							new AntPathRequestMatcher("/images/**")).permitAll() // 모든 사용자가 로그인 없이 해당 경로로 접근이 가능
					.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN") // /admin으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우 접근이 가능
					.anyRequest().authenticated()); // 위에 설정한 경로를 제외한 나머지 경로들은 모두 로그인을 요구
		
		http.exceptionHandling(AuthenticationManager -> 
					AuthenticationManager.authenticationEntryPoint(new CustomAuthenticationEntryPoint())); // 로그인 되지 않은 사용자가 리소스에 접근 시 수행되는 핸들러를 등록
		
		return http.build();
	}
	
	/*
	public AuthenticationManager authenticationManager (AuthenticationManagerBuilder auth) throws Exception
	{
		// 인증은 AuthenticationManager을 통해 이루어지며 Builder가 AuthenticationManager를 생성
		// userDetailService를 구현하고 있는 객체를 memberService를 지정 하고 비밀번호 암호화
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
		return auth.build();
	}
	*/
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(); // 비밀번호를 암호화 하여 데이터베이스에 저장
	}
}