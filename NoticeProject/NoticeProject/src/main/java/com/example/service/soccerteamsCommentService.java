package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.soccerteam_CommentDto;
import com.example.entity.soccerteams;
import com.example.entity.soccerteams_Comment;
import com.example.repository.soccerteamsCommentRepository;
import com.example.repository.soccerteamsRepository;

@Service
public class soccerteamsCommentService 
{
	@Autowired
	private soccerteamsCommentRepository soccerteamscommentrepository;
	@Autowired
	private soccerteamsRepository soccerteamsrepository;
	
	public List<soccerteams_Comment> comments(Long id) // 댓글 목록
	{
		List<soccerteams_Comment> soccerteamscomments = soccerteamscommentrepository.findBySoccerteamsId(id);
		return soccerteamscomments;
	}
	
	@Transactional
	public soccerteams_Comment create(Long id, soccerteam_CommentDto dto) // 댓글 생성
	{
		soccerteams soccerteamsEntity = soccerteamsrepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("댓글 생성 실패!" + "대상 게시글이 없습니다."));
		
		soccerteams_Comment soccerteamsCommentEntity = soccerteams_Comment.createComment(dto, soccerteamsEntity);
		return soccerteamscommentrepository.save(soccerteamsCommentEntity);
	}
	
	@Transactional
	public soccerteams_Comment update(Long id, soccerteam_CommentDto dto) // 댓글 수정
	{
		soccerteams_Comment target = soccerteamscommentrepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("댓글 수정 실패!" + "대상 댓글이 없습니다."));
		
		soccerteams_Comment soccerteamsEntity = dto.toEntity();
		target.patch(soccerteamsEntity);
		
		return soccerteamscommentrepository.save(target);
	}
	
	@Transactional
	public soccerteams_Comment delete(Long id) // 댓글 삭제
	{
		soccerteams_Comment target = soccerteamscommentrepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("대상 댓글이 없습니다."));
		soccerteamscommentrepository.delete(target);
		return target;
	}
}