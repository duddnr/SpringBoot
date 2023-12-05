package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.soccerteamDto;
import com.example.service.soccerteamsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class soccerteamController 
{
	@Autowired
	private soccerteamsService soccerteamsservice;
	
	@GetMapping("/soccerteams") // 게시글 목록 조회
	public String index(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
	{
		return soccerteamsservice.index(model, pageable);
	}
	
	@GetMapping("/soccerteam/{id}") // 게시글 상세 보기
	public String show(@PathVariable Long id, Model model)
	{
		return soccerteamsservice.show(id, model);
	}
	
	@GetMapping("/soccerteam/read/{id}") // 게시글 조회수 올리기
	public String read(@PathVariable Long id)
	{
		return soccerteamsservice.read(id);
	}
	
	@GetMapping("/soccerteam/new") // 게시글 입력 페이지
	public String newSoccerTeam()
	{
		return "/soccerteam/new";
	}
	
	@PostMapping("/soccerteam/create") // 입력 페이지에서 폼 데이터를 전달 받고 데이터 생성
	public String create(soccerteamDto dto)
	{
		return soccerteamsservice.create(dto);
	}
	
	@GetMapping("/soccerteam/{id}/edit") // 게시글 수정 페이지
	public String edit(@PathVariable Long id, Model model)
	{
		return soccerteamsservice.edit(id, model);
	}
	
	@PostMapping("/soccerteam/update") // 수정 페이지에서 폼 데이터를 전달 받고 데이터 수정
	public String update(soccerteamDto dto)
	{
		return soccerteamsservice.update(dto);
	}
	
	@GetMapping("/soccerteam/{id}/delete") // 게시글 삭제하기
	public String delete(@PathVariable Long id, RedirectAttributes rttr)
	{	
		return soccerteamsservice.delete(id, rttr);
	}
}