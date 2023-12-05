package com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.security.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> 
{
	@Query("select i from Member i where i.userId=%:userId% or i.email=%:email% or i.phoneNumber=%:phoneNumber%")
	Member findByuserIdOremailOrphoneNumber(@Param("userId") String userId, @Param("email") String email, @Param("phoneNumber") String phoneNumber);
	
	Member findByuserId(String userId);
}