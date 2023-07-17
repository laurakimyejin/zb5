package com.zerobase.application.dto.response;

import com.zerobase.domain.RoleType;
import com.zerobase.domain.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data //getter, setter
public class UserResponseDto {
	
	private Long idx; //식별자
	
	@NotBlank
	@Size(min = 4, max = 12)
	@NotEmpty(message = "아이디 입력은 필수입니다.")
	private String userid;

	@NotBlank
	private String username;
	
	@NotBlank
	//@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String password;
	
	@NotEmpty(message = "전화번호 입력은 필수입니다.")
	//@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
	private String phoneNumber;
	
	private RoleType role;
	
	@Builder
	public UserResponseDto(Long idx, String userid, String username, String password, String phoneNumber, RoleType role) {
		this.idx = idx;
		this.userid = userid;
		this.username=username;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}
	
	public User toDto() {
		return User.builder()
				   .idx(idx)
				   .userid(userid).username(username)
				   .password(password)
				   .phoneNumber(phoneNumber)
				   .build();
	}
}
