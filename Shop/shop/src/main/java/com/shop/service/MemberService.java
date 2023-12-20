package com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;

@Transactional
@Service
public class MemberService implements UserDetailsService // UserDetailsService = 데이터베이스에서 회원 정보를 가져오는 역할
{
	@Autowired
	private MemberRepository memberRepository;
	
	public Member saveMember(Member member)
	{
		validateDuplicateMember(member);
		return memberRepository.save(member);
	}
	
	private void validateDuplicateMember(Member member) throws IllegalStateException
	{
		Member findMember = memberRepository.findByemail(member.getEmail());
		if(findMember != null)
		{
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}
	
	@Override // UserDetailsService 인터페이스의 loadUserByUsername 메소드를 오버라이딩함
	public UserDetails loadUserByUsername(String email)
	{
		Member member = memberRepository.findByemail(email);
		
		if(member == null)
		{
			throw new UsernameNotFoundException(email);
		}
		
		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}
}