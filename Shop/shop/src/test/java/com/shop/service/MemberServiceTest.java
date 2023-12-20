package com.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;

@Transactional
@SpringBootTest
public class MemberServiceTest 
{
	@Autowired
	MemberService memberService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	@DisplayName("회원가입 테스트")
	public void saveMemberTest()
	{
		Member member = createMember();
		Member savedMember = memberService.saveMember(member);
		
		assertEquals(member.getName(), savedMember.getName());
		assertEquals(member.getEmail(), savedMember.getEmail());
		assertEquals(member.getPassword(), savedMember.getPassword());
		assertEquals(member.getAddress(), savedMember.getAddress());
		assertEquals(member.getRole(), savedMember.getRole());
	}
	
	public Member createMember()
	{
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setName("홍길동");
		memberFormDto.setEmail("test@email.com");
		memberFormDto.setAddress("서울시 마포구 합정동");
		memberFormDto.setPassword("1122");
		return Member.createMember(memberFormDto, passwordEncoder);
	}
	
	@Test
	@DisplayName("중복 회원가입 테스트")
	public void saveDuplicateMemberTest()
	{
		Member member1 = createMember();
		Member member2 = createMember();
		memberService.saveMember(member1);
		
		Throwable e = assertThrows(IllegalStateException.class, () -> { memberService.saveMember(member2); });
		
		assertEquals("이미 가입된 회원입니다.", e.getMessage());
	}
}