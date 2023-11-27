package com.security.dto;

import org.hibernate.validator.constraints.Length;

import com.security.constant.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberDto 
{
	@NotBlank(message="이름은 필수 입력값 입니다.")
	private String name; // 이름
	
	@NotBlank(message="아이디는 필수 입력값 입니다.")
	private String userId; // 아이디
	
	@NotEmpty(message="이메일은 필수 입력값 입니다.")
	private String email; // 이메일
	
	@NotEmpty(message="비밀번호는 필수 입력값 입니다.")
	@Length(min=8, max=16, message="비밀번호는 8자 이상, 16자 이하로 입력해주세요")
	private String password; // 비밀번호
	
	@NotEmpty(message="핸드폰 번호는 필수 입력값 입니다.")
	@Pattern(regexp="^\\d{2,3}-\\d{3,4}-\\d{4}$", message="핸드폰 번호 양식에 맞지 않습니다 (010-xxxx-xxxx)")
	private String phoneNumber; // 핸드폰 번호
	
	@NotBlank(message="주소는 필수 입력값 입니다.")
	private String address; // 주소
	
	@NotNull(message="성별은 필수 입력값 입니다.")
	private Gender gender; // 성별
}