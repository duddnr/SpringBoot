package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.soccerteams_Comment;


public interface soccerteamsCommentRepository extends JpaRepository<soccerteams_Comment, Long> 
{
	List<soccerteams_Comment> findBySoccerteamsId(Long soccerteamsId);
}