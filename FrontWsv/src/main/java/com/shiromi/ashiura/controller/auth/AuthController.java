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
    public String login() {
        log.info("View: {}", urlApi + "/auth/login");
        return "auth/loginForm";
    }
    // 회원가입 뷰 띄우기
    @GetMapping("/auth/signup")
    public String signup() {
        log.info("View: {}", urlApi + "/auth/signup");
        return "auth/signup";
    }
    //웹에서 로그인 요청 처리하는 메소드
    @PostMapping("/auth/loginForm")
    public String loginForm(UserLoginRequest userLoginRequestXML, HttpServletResponse response) throws URISyntaxException {
        log.info("Post: {}", urlApi + "/auth/loginForm");
        log.info("data: {}", userLoginRequestXML.toString());

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

        return "/justwait";

    }

    // Authorized 헤더에 AccessToken 값이 없거나 헤더에 대한 정보가 없는 경우
    // AccessToken이 유효하지 않은 토큰인 경우
    // AccessToken의 시간이 만료된 경우

    // AccessToken이 만료되지 않았을 때 RefreshToken을 사용해 재발급 받는 경우는 해당 공격자가 탈취를 한경우라고 판단 두 토믄을 모두 만료시키는 방식
    // RefreshToken이 cookie에 존재하지 않는경우
    // RefreshToken으로 DB에 유저정보를 검색하는데, 이 토큰을 가지고 있는 사용자가 없는경우
    // RefreshToken이 유효하지 않은 토큰인 경우
    // RefreshToken의 시간이 만료된 경우

    // 이거 사실 프론트가 해야 될 일이 아닐까?
    // 자바스크립트로 localStorage에 set 해야 페이지 이동시 쿠키가 소실 되지 않음, 프론트 일 맞는듯

    @PostMapping("/auth/signupForm")
    public String saveForm(UserDomain userDomain) {
        log.info("post: {}", urlApi + "/auth/signupForm");
        log.info("UsDo: {}", userDomain.toString());
        log.info("save: {}", userService.userSave(userDomain));

        return "/auth/login";
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(userDomain.toString());
    }
    @GetMapping("/auth/logout")
    public String logout(
            @CookieValue(value = "Bearer", required = false) String token,
            HttpServletResponse response) {
        log.info("logout: {}", urlApi + "/auth/logout");

        ResponseCookie cookie = ResponseCookie.from("Bearer", token)
                .maxAge(0)
                .path("/")
                .secure(false)
                .sameSite("Lax")
                .httpOnly(true)
                .build();

        log.info(cookie.toString());
        response.addHeader("Set-Cookie",cookie.toString());


        return "/justwait";
    }



//    @PostMapping("/signup")
//    public ResponseEntity<?> save(Model model) {
//        log.info("post: {}", urlCli + "/signup");
//        log.info("modD: {}", model.toString());
//        log.info("save: {}", userService.userSave(model));
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(model.toString());
//    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> save(
//            @RequestParam("userName") String userName,
//            @RequestParam("password") String password,
//            @RequestParam("phoneNumber") String phoneNumber
//    ) {
//        log.info("post: {}", urlCli + "/signup");
//        log.info("modD: {}", userName +"~"+ password +"~"+ phoneNumber);
//        log.info("save: {}", userService.userSave(userName, password, phoneNumber));
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(userName +"~"+ password +"~"+ phoneNumber);
//    }




//    @PostMapping("/signup")
//    public ResponseEntity<?> save (
//            @RequestBody Map<String, Object> resultMap
//    ){
//        log.info((String) resultMap.get("id"));
////        String join = userService.join(userDomain);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body("");
//    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> save(
//            @RequestParam("id") String user_id,
//            @RequestParam("password") String password,
//            @RequestParam("name") String name,
//            @RequestParam("phone") String phone) {
//        UserDomain userDomain = UserDomain
//                .builder()
//                .userId(user_id)
//                .password(password)
//                .username(name)
//                .phoneNumber(phone)
//                .rating("N")
//                .build();
//
//
//        String join = userService.join(userDomain);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(join);
//    }



//    @GetMapping("/User/view")
//    public String view(Model model) {
//
//        List<UserEntity> users = UserRepository.findAll();
//        model.addAttribute("all",users);
//        return "view";
//    }
}
