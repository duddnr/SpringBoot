package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dto.soccerteamDto;
import com.example.entity.soccerteams;
import com.example.entity.soccerteams_Comment;
import com.example.repository.soccerteamsCommentRepository;
import com.example.repository.soccerteamsRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class soccerteamsService 
{
	@Autowired
	private soccerteamsRepository soccerteamsrepository;
	@Autowired
	private soccerteamsCommentService soccerteamscommentservice;
	
	public String index(Model model, Pageable pageable) // 게시글 목록
	{	
		Page<soccerteams> soccerteamsList = commentCheck(pageable);
		
		//List<soccerteams> soccerteamsEntityList = soccerteamsrepository.findAll();
		model.addAttribute("soccerteamsList", soccerteamsList);
		model.addAttribute("prev", pageable.previousOrFirst().getPageNumber());
		model.addAttribute("next", pageable.next().getPageNumber());
		model.addAttribute("hasPrev", soccerteamsList.hasPrevious());
		model.addAttribute("hasNext", soccerteamsList.hasNext());
		return "/soccerteam/index";
	}
	
	public Page<soccerteams> commentCheck(Pageable pageable) // 게시글 댓글 수 확인
	{
		List<soccerteams> list = soccerteamsrepository.findAll();
		
		for(soccerteams target : list)
		{
			List<soccerteams_Comment> commentList = soccerteamscommentservice.comments(target.getId());
			
			if(!commentList.isEmpty())
			{
				target.setComment_(Long.valueOf(commentList.size()));
				soccerteamsrepository.save(target);
			}
			else
			{
				target.setComment_(null);
				soccerteamsrepository.save(target);
			}
		}
		
		return soccerteamsrepository.findAll(pageable);
	}
	
	public String show(Long id, Model model) // 게시글 상세 보기
	{
		soccerteams soccerteamEntity = soccerteamsrepository.findById(id).orElse(null);
		List<soccerteams_Comment> commentList = soccerteamscommentservice.comments(id);
		model.addAttribute("soccerteams", soccerteamEntity);
		model.addAttribute("soccerteamsComment", commentList);
		return "/soccerteam/show";
	}
	
	@Transactional
	public String read(Long id) // 게시글 조회 수 올리기
	{
		soccerteamsrepository.updateView(id);
		return "redirect:/soccerteam/" + id;
	}
	
	public String create(soccerteamDto dto) // 게시글 생성
	{
		soccerteams soccerEntity = dto.toEntity();	
		soccerteamsrepository.save(soccerEntity);
		return "redirect:/soccerteams";
	}
	
	public String edit(Long id, Model model) // 게시글 수정
	{
		soccerteams soccerteamEntity = soccerteamsrepository.findById(id).orElse(null);
		model.addAttribute("soccerteams", soccerteamEntity);
		return "/soccerteam/edit";
	}
	
	@Transactional
	public String update(soccerteamDto dto) // 게시글 수정 데이터 DB에 넣기
	{
		soccerteams target = dto.toEntity();
		soccerteams soccerteamEntity = soccerteamsrepository.findById(target.getId()).orElse(null);
		soccerteamEntity.patch(target);
		soccerteamsrepository.save(soccerteamEntity);
		return "redirect:/soccerteam/" + soccerteamEntity.getId();
	}
	
	@Transactional
	public String delete(Long id, RedirectAttributes rttr) // 게시글 삭제
	{
		soccerteams target = soccerteamsrepository.findById(id).orElse(null);
		if(target != null)
		{
			rttr.addFlashAttribute("msg", "삭제됐습니다.");
			soccerteamsrepository.delete(target);
		}
		return "redirect:/soccerteams";
	}
	
}