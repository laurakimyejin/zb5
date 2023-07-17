package com.zerobase.application.dto.request;

import com.zerobase.domain.User;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class UserUpdateDto {
	
	private Long idx;

	private String userid;
	private String username;
	
	@NotBlank
	private String password;
	
	@NotEmpty(message = "전화번호 입력은 필수입니다.")
	private String phoneNumber;
	
	@Builder
	public UserUpdateDto(Long idx, String userid, String username, String password, String phoneNumber) {
		this.idx = idx;
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
	}

	public User toEntity() {
		return User.builder()
				   .idx(idx).userid(userid)
				   .username(username)
				   .password(password)
				   .phoneNumber(phoneNumber)
				   .build();
	}
}
