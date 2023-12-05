package com.example.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.soccerteams;

public interface soccerteamsRepository extends JpaRepository<soccerteams, Long>
{

	@Override
	ArrayList<soccerteams> findAll();
	
	@Override
	Page<soccerteams> findAll(Pageable pageable); // 설정한 페이지에 객체를 담아서 반환
	
	@Modifying
	@Query("update soccerteams p set p.views = p.views + 1 where p.id = :id")
	int updateView(Long id);
}