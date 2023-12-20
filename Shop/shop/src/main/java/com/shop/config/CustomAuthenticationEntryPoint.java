package com.shop.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint 
{
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException
	{
		if("XMLHttpRequest".equals(request.getHeader("x-requested-with")))
		{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}
		else
		{
			response.sendRedirect("/members/login");
		}
	}
}