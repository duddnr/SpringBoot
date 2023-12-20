package com.shop.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@SpringBootTest
public class ItemControllerTest 
{
	@Autowired
	MockMvc mockMvc;
	
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN") // 현재 회원 이름이 admin이고 권한이 ADMIN 유저가 로그인된 상태로 테스트
	@DisplayName("상품 등록 페이지 권한 테스트")
	public void itemFormTest() throws Exception
	{
		// 상품 등록 페이지에 Get 요청을 보내고 요청과 응답 메시지를 확인할 수 있도록 콘솔창에 출력
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "user", roles = "USER") // 현재 회원 이름이 admin이고 권한이 ADMIN 유저가 로그인된 상태로 테스트
	@DisplayName("상품 등록 페이지 일반 회원 권한 테스트")
	public void itemFormNotAdminTest() throws Exception
	{
		// 상품 등록 페이지에 Get 요청을 보내고 요청과 응답 메시지를 확인할 수 있도록 콘솔창에 출력
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new")).andDo(print()).andExpect(status().isForbidden());
	}
}