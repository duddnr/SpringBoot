package com.shop.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.shop.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Transactional
@SpringBootTest
public class MemberTest 
{
	@Autowired
	MemberRepository memberRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("Auditing 테스트")
	@WithMockUser(username="gildong", roles="USER") // 스프링 시큐리티에서 제공되는 어노테이션, 해당 사용자가 로그인한 상태라고 가정함
	public void auditingTest()
	{
		Member newMember = new Member();
		memberRepository.save(newMember);
		
		em.flush();
		em.clear();
		
		Member member = memberRepository.findById(newMember.getId()).orElseThrow(EntityNotFoundException::new);
		
		System.out.println("register time : " + member.getRegTime());
		System.out.println("update time : " + member.getUpdateTime());
		System.out.println("create member : " + member.getCreatedBy());
		System.out.println("modify member : " + member.getModifiedBy());
	}
}