package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.dto.MemberDto;
import com.security.service.MemberService;

import jakarta.validation.Valid;

@Controller
public class MemberController 
{
	@Autowired
	MemberService memberService;
	
	@GetMapping("/main") // 메인 페이지
	public String main()
	{
		return "/member/main";
	}
	
	@GetMapping("/login") // 로그인 페이지
	public String login()
	{
		return "/member/memberLogin";
	}
	
	@GetMapping("/login/error") // 로그인 에러 페이지
	public String loginError(Model model)
	{
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인 해주세요.");
		return "/member/memberLogin";
	}
	
	@GetMapping("/join") // 회원가입 페이지
	public String join(MemberDto memberDto, Model model)
	{
		model.addAttribute("memberDto", memberDto);
		return "/member/memberJoin";
	}
	
	@PostMapping("/join") // 회원가입 데이터 받기
	public String newJoin(@Valid MemberDto memberDto, BindingResult bindingResult, Model model)
	{
		if(bindingResult.hasErrors()) { return "/member/memberJoin"; }
		
		try
		{
			memberService.created(memberDto);
		}
		catch(IllegalStateException e)
		{
			model.addAttribute("errorMessage", e.getMessage());
			return "/member/memberJoin";
		}
		
		return "redirect:/main";
	}
}