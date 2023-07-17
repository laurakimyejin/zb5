package com.zerobase.domain;

import com.zerobase.application.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idx;
	
	@Column(unique = true, nullable = false, length = 100, name="user_id")
	private String userid;
	
	@Column(nullable = false, length = 100)
	private String password;

	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false, length = 50)
	private String phoneNumber;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	private String provider;
	private String providerId;
	
	public void updatePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void updatePassword(String password) {
		this.password = password;
	}
	
	public void updateRole(RoleType role) {
		this.role = role;
	}
	
    public UserResponseDto toDto() {
        return UserResponseDto.builder()
                .idx(idx)
                .userid(userid)
                .password(password)
				.username(username)
                .phoneNumber(phoneNumber)
                .build();
    }
    
    @Builder
    public User(long idx, String userid, String password, String username, String phoneNumber, RoleType role, String provider, String providerId) {
        this.idx = idx;
        this.userid = userid;
        this.password = password;
		this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
