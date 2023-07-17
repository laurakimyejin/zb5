package com.shiromi.ashiura.domain.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserLoginRequest {

    private String userName;
    private String password;

    @Builder
    public UserLoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}