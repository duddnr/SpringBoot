package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.dto.MemberDto;
import com.security.entity.Member;
import com.security.repository.MemberRepository;

@Service
public class MemberService implements UserDetailsService
{
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public void created(MemberDto memberDto)
	{
		Member member = Member.createdMember(memberDto, passwordEncoder);
		uniqueTest(memberDto); // 중복 검사 (중복 발견 시 에러 발생)
		
		memberRepository.save(member);
	}
	
	public void uniqueTest(MemberDto memberDto) throws IllegalStateException
	{
		Member member = memberRepository.findByuserIdOremailOrphoneNumber(memberDto.getUserId(), memberDto.getEmail(), memberDto.getPhoneNumber());
		
		if(member != null)
		{	
			if(member.getUserId().equals(memberDto.getUserId())) { throw new IllegalStateException("이미 가입된 아이디 입니다.");  }
			else if(member.getEmail().equals(memberDto.getEmail())) { throw new IllegalStateException("이미 가입된 이메일 입니다."); }
			else if(member.getPhoneNumber().equals(memberDto.getPhoneNumber())) { throw new IllegalStateException("이미 가입된 핸드폰 번호 입니다."); }
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId)
	{
		Member member = memberRepository.findByuserId(userId);
		if(member == null) { throw new UsernameNotFoundException(userId); }
		
		return User.builder()
				.username(member.getUserId())
				.password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}
}