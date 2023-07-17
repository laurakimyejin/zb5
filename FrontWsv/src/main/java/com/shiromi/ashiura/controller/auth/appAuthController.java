package com.shiromi.ashiura.controller.auth;

import com.shiromi.ashiura.domain.dto.TokenInfo;
import com.shiromi.ashiura.domain.dto.UserDomain;
import com.shiromi.ashiura.domain.dto.request.UserLoginRequest;
import com.shiromi.ashiura.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class appAuthController {
    private final UserService userService;

    @Value("${url.api}")
    private String urlApi;

    //앱에서 로그인 요청을 처리하는 API
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequestJson) {
        log.info("Post: {}", urlApi + "/auth/login");
        log.info("data: {}", userLoginRequestJson);
        TokenInfo tokenInfo = userService.userLogin(userLoginRequestJson);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/auth/signup")
    public ResponseEntity<?> save(@RequestBody UserDomain userDomain) {
        log.info("post: {}", urlApi + "/auth/signup");
        log.info("UsDo: {}", userDomain.toString());
        log.info("save: {}", userService.userSave(userDomain));

        return ResponseEntity.status(HttpStatus.OK)
                .body(userDomain.toString());
    }

}
