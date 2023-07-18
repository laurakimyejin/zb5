package com.shiromi.ashiura.controller.auth;

import com.shiromi.ashiura.domain.dto.TokenInfo;
import com.shiromi.ashiura.domain.dto.UserDomain;
import com.shiromi.ashiura.domain.dto.request.UserLoginRequest;
import com.shiromi.ashiura.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class appAuthController {
    private final UserService userService;

    @Value("${url.api}")
    private String urlApi;

//    앱에서 로그인 요청을 처리하는 API
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequestJson) {
        log.info("Post: {}", urlApi + "/auth/login");
        log.info("data: {}", userLoginRequestJson);
        TokenInfo tokenInfo = userService.userLogin(userLoginRequestJson);
        return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
    }

    //앱에서 로그인 받는 부분 쿠키 반환으로 대체, 다만 토큰자체를 보낼껀지 쿠키로 포장해서 보낼껀지에 대한 조율 필요
    //이후 신고 할때 아이디만 받는 부분도 그대로 가져갈지 아니면 토큰or쿠키를 실어서 보낼지 조율필요
//    @PostMapping("/auth/login")
//    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequestJson) {
//        log.info("Post: {}", urlApi + "/auth/login");
//        log.info("data: {}", userLoginRequestJson);
//        TokenInfo tokenInfo = userService.userLogin(userLoginRequestJson);
//
//        ResponseCookie cookie = ResponseCookie.from(tokenInfo.getGrantType(), tokenInfo.getAccessToken())
//                .maxAge(3600)
//                .path("/")
//                .secure(false) // ture = url이 https가 아니면 쿠키 저장안하는 기능, 실제 배포시에는 https를 써서 데이터 암호화를 해야 보안이슈가 생길 가능성이 없다
//                .sameSite("Lax") //서드파티 보안문제
//                .httpOnly(true) // ture = JS가 읽어내지 못하게함,  CSS취약점 문제해결
//                .build();
//
//        log.info(cookie.toString());
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(cookie);
//    }

    //앱에서 회원가입 부분
    @PostMapping("/auth/signup")
    public ResponseEntity<?> save(@RequestBody UserDomain userDomain) {
        log.info("post: {}", urlApi + "/auth/signup");
        log.info("UsDo: {}", userDomain.toString());
        log.info("save: {}", userService.userSave(userDomain));

        return ResponseEntity.status(HttpStatus.OK)
                .body(userDomain.toString());
    }

}
