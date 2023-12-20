package com.shop.entity;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;

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
import lombok.ToString;

@Table(name="member")
@SequenceGenerator(name = "MEMBER_SEQ", sequenceName = "member_seq", initialValue = 1, allocationSize = 1)
@ToString
@Data
@Entity
public class Member extends BaseEntity
{
	@Id
	@Column(name="member_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MEMBER_SEQ")
	private Long id;
	
	private String name;
	
	@Column(unique = true) // 중복 방지
	private String email;
	
	private String password;
	
	private String address;
	
	@Enumerated(EnumType.STRING) // Enum 타입이 String으로 저장됨
	private Role role;
	
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder)
	{
		Member member = new Member();
		
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		String password = passwordEncoder.encode(memberFormDto.getPassword()); // 비밀번호 암호화
		member.setPassword(password);
		member.setRole(Role.USER);
		
		return member;
	}
}