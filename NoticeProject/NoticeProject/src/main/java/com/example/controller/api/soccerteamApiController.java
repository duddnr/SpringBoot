package com.example.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.soccerteam_CommentDto;
import com.example.entity.soccerteams_Comment;
import com.example.service.soccerteamsCommentService;

@RestController
public class soccerteamApiController 
{
	@Autowired
	private soccerteamsCommentService soccerteamscommentservice;
	
	// 댓글 조회
	@GetMapping("/api/soccerteam/{soccerteamsId}/comments")
	public ResponseEntity<List<soccerteams_Comment>> comments(@PathVariable Long soccerteamsId)
	{
		return ResponseEntity.status(HttpStatus.OK).body(soccerteamscommentservice.comments(soccerteamsId));
	}
	
	// 댓글 생성
	@PostMapping("/api/soccerteam/{soccerteamsId}/comments")
	public ResponseEntity<soccerteams_Comment> create(@PathVariable Long soccerteamsId, @RequestBody soccerteam_CommentDto dto)
	{
		return ResponseEntity.status(HttpStatus.OK).body(soccerteamscommentservice.create(soccerteamsId, dto));
	}
	
	// 댓글 수정
	@PatchMapping("/api/comments/{soccerteamsId}")
	public ResponseEntity<soccerteams_Comment> update(@PathVariable Long soccerteamsId, @RequestBody soccerteam_CommentDto dto)
	{
		return ResponseEntity.status(HttpStatus.OK).body(soccerteamscommentservice.update(soccerteamsId, dto));
	}
	
	
	// 댓글 삭제
	@DeleteMapping("/api/comments/{soccerteamsId}")
	public ResponseEntity<soccerteams_Comment> delete(@PathVariable Long soccerteamsId)
	{
		return ResponseEntity.status(HttpStatus.OK).body(soccerteamscommentservice.delete(soccerteamsId));
	}
}