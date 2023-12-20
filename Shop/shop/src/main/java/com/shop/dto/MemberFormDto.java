package com.shop.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberFormDto 
{
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name; // 이름
	
	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	private String email; // 이메일
	
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	@Length(min=8, max=16, message="비밀번호는 8자 이상, 16자 이하로 입력해주세요")
	private String password; // 비밀번호
	
	@NotEmpty(message = "주소는 필수 입력 값입니다.")
	private String address; // 주소
}