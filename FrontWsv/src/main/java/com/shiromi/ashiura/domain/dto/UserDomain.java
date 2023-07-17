package com.shiromi.ashiura.domain.dto;

import com.shiromi.ashiura.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class UserDomain {

    private String userName;
    private String name;
    private String password;
    private String phoneNumber;
    private String rating;

    @Builder
    public UserDomain(String userName, String name, String password, String phoneNumber, String rating) {

        this.userName = userName;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
    }
    public UserEntity toEntity() {
        return UserEntity.builder()
                .userName(userName)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .rating(rating)
                .build();
    }

//    public void login(Password rawPassword, PasswordEncoder passwordEncoder) {
//        this.password.matchPassword(rawPassword, passwordEncoder)
//    }
}