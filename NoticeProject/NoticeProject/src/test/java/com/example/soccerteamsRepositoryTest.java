package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.soccerteams;
import com.example.repository.soccerteamsRepository;

@SpringBootTest
public class soccerteamsRepositoryTest 
{
	@Autowired
	private soccerteamsRepository soccerteamsrepository;
	
	@Test
	@Transactional
	public void testPageDefault()
	{
		Pageable pageable = PageRequest.of(0, 10); // 1 페이지에 데이터 10개를 가져온다.
		Page<soccerteams> result = soccerteamsrepository.findAll(pageable);
		
		System.out.println(result);
		System.out.println("전체 페이지 : " + result.getTotalPages());
		System.out.println("전체 데이터 개수 : " + result.getTotalElements());
		System.out.println("현재 페이지 번호(0부터 시작) : " + result.getNumber());
		System.out.println("페이지 당 데이터 개수 : " + result.getSize());
		System.out.println("다음 페이지 존재 여부 : " + result.hasNext());
		System.out.println("시작 페이지(0페이지) 여부 : " + result.isFirst());
		
		for(soccerteams soccerteam : result.getContent())
		{
			System.out.println("soccerteams id : " + soccerteam.getId());
		}
	}
	
	@Test
	@Transactional
	public void testSort()
	{
		for(Long i = 1L; i <= 100; i++)
		{
			//soccerteams soccerteamEntity = new soccerteams(null, "test", "test" + i, "test 입니다", 0L, null);
			//soccerteamsrepository.save(soccerteamEntity);
		}
		
		Sort sort1 = Sort.by("id").descending(); // id 기준 역순으로 출력
		Pageable pageable = PageRequest.of(0, 10, sort1);
		Page<soccerteams> result = soccerteamsrepository.findAll(pageable);
		
		for(soccerteams soccerteam : result.getContent())
		{
			System.out.println("soccerteams id : " + soccerteam.getId());
		}
	}
	
	@Test
	public void create()
	{
		for(Long i = 1L; i <= 100; i++)
		{
			String created = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"));
			//soccerteams soccerteamEntity = new soccerteams(null, "test", "test" + i, "test 입니다", 0L, created);
			//soccerteamsrepository.save(soccerteamEntity);
		}
	}
	
	@Test
	public void dd()
	{
		A a = new A();
		B b = new B();
		if(a.getX() != b.getY())
		{
			System.out.println("aa");
		}
		else
		{
			System.out.println("bb");
		}
	}
}