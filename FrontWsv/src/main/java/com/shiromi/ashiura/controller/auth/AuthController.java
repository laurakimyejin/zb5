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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {
    private final UserService userService;

    @Value("${url.api}")
    private String urlApi;

    // 로그인 뷰 띄우기
    @GetMapping("/auth/loginForm")
    public String login(Model model) {
        log.info("View: {}", urlApi + "/auth/login");
        model.addAttribute("userName", "unknown");
        return "auth/loginForm";
    }
    // 회원가입 뷰 띄우기
    @GetMapping("/auth/signup")
    public String signup(Model model) {
        log.info("View: {}", urlApi + "/auth/signup");
        model.addAttribute("userName", "unknown");
        return "auth/signup";
    }
    //웹에서 로그인 요청 처리하는 메소드
    @PostMapping("/auth/loginForm")
    public String loginForm(UserLoginRequest userLoginRequestXML, HttpServletResponse response) {
        log.info("Post: {}", urlApi + "/auth/loginForm");
        log.info("data: {}", userLoginRequestXML.toString());
        //토큰 발급
        TokenInfo tokenInfo = userService.userLogin(userLoginRequestXML);
        //쿠키 생성
        ResponseCookie cookie = ResponseCookie.from(tokenInfo.getGrantType(), tokenInfo.getAccessToken())
                .maxAge(3600)
                .path("/")
                .secure(false) // ture = url이 https가 아니면 쿠키 저장안하는 기능, 실제 배포시에는 https를 써서 데이터 암호화를 해야 보안이슈가 생길 가능성이 없다
                .sameSite("Lax") //서드파티 보안문제
                .httpOnly(true) // ture = JS가 읽어내지 못하게함,  CSS취약점 문제해결
                .build();

        log.info(cookie.toString());
        response.addHeader("Set-Cookie",cookie.toString());
        //쿠키로 로그인적용을 위한 임시 페이지
        return "/justwait";

    }
    //웹에서의 회원가입 부분
    @PostMapping("/auth/signupForm")
    public String saveForm(UserDomain userDomain) {
        log.info("post: {}", urlApi + "/auth/signupForm");
        log.info("UsDo: {}", userDomain.toString());
        log.info("save: {}", userService.userSave(userDomain));

        return "/auth/loginForm";
    }
}
