package com.security.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.security.constant.Gender;
import com.security.constant.Role;
import com.security.dto.MemberDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name="member")
@SequenceGenerator(name = "MEMBER_SEQ", sequenceName = "member_seq", initialValue = 1, allocationSize = 1)
@Data
@Entity
public class Member 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MEMBER_SEQ") // 시퀀스 전략
	@Column(name="member_id")
	private Long id; // 시퀀스 아이디
	
	private String name; // 이름
	
	@Column(unique = true) // 중복 방지
	private String userId; // 아이디
	
	@Column(unique = true)
	private String email; // 이메일
	
	private String password; // 비밀번호
	
	private String address; // 주소
	
	@Column(unique = true)
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING) // 열거형을 String 타입을 저장
	private Gender gender; // 성별
	
	@Enumerated(EnumType.STRING)
	private Role role; // 권한
	
	public static Member createdMember(MemberDto memberDto, PasswordEncoder passwordEncoder)
	{
		Member member = new Member();
		member.setName(memberDto.getName());
		member.setUserId(memberDto.getUserId());
		member.setEmail(memberDto.getEmail());
		member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
		member.setAddress(memberDto.getAddress());
		member.setGender(memberDto.getGender());
		member.setRole(Role.USER);
		return member;
	}
}