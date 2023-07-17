package com.shiromi.ashiura.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public  enum RoleType {
	//권한 생성
	ROLE_NORMAL_USER("N"),
	ROLE_ADMIN("Y");
	private String value;
}
