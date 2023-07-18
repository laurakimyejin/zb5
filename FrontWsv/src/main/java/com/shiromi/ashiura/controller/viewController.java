package com.shiromi.ashiura.controller;

import com.shiromi.ashiura.config.jwt.JwtProvider;
import com.shiromi.ashiura.domain.entity.VoiceDataEntity;
import com.shiromi.ashiura.service.LoadingService;
import com.shiromi.ashiura.service.VoiceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class viewController {

    private final LoadingService loadingService;
    private final JwtProvider jwtProvider;
    private final VoiceDataService voiceDataService;


    @Value("${url.api}")
    private String urlApi;

    @GetMapping("/")
    public String home(
//            @CookieValue(value = "Bearer", required = false) String token,
            @AuthenticationPrincipal User user, //url을 입력하면 스프링 시큐리티에서 헤더로 전달된 쿠키(토큰)을 읽어서 사용자를 확인하는데, 그 인증과정에서 사용한 객체를 주입함
            Model model)   {
        log.info("View: {}", urlApi + "/");
//        if (token != null) {
//            log.info("token: {}",token);
//            String user = jwtProvider.showClaims(token);
        if (user != null) {
            model.addAttribute("userName", user.getUsername());
        } else {
            model.addAttribute("userName", "unknown");
        }
        return "home";
    }
    //신고 내역 뷰 반환
    @GetMapping("/view/info")
    public String view_user_info(
            @AuthenticationPrincipal User user,
            Model model) {
        log.info("View: {}", urlApi + "/view/info");
        if (user != null) {
            List<VoiceDataEntity> voiceData = voiceDataService.findByUserNameAll(user.getUsername());

            model.addAttribute("userName", user.getUsername());
            model.addAttribute("voiceData", voiceData);
        } else {
            model.addAttribute("userName", "unknown");
        }
        return "view/user_info";
    }
    //로딩창 뷰 반환, 항상 STT변환이 제일 오래걸려서 40퍼에서 3분쯤 멍때릴듯
    @GetMapping("/view/loading")
    public String view_loading(
            @AuthenticationPrincipal User user,
            Model model) throws InterruptedException {
        log.info("Get: {}", urlApi + "/view/loading");
        model.addAttribute("result",loadingService.showLoadingView());
        if (user != null) {
            model.addAttribute("userName", user.getUsername());
        } else {
            model.addAttribute("userName", "unknown");
        }
        return "view/Loading";
    }
    // 테스트용 통화 테이블데이터 추가 뷰 반환
    @GetMapping("/test/addVoi")
    public String addVoiceData(
            @AuthenticationPrincipal User user,
            Model model) {
        log.info("Get: {}", urlApi + "/test/add_voice_data");
        if (user != null) {
            model.addAttribute("userName", user.getUsername());
        } else {
            model.addAttribute("userName", "unknown");
        }
        return "test/add_voice_data";
    }
    // 웹에서 신고하기 뷰 반환
    @GetMapping("/view/VoiClaReq")
    public String VoiceClientRequest(
            @AuthenticationPrincipal User user,
            Model model) {
        log.info("Get: {}", urlApi + "/Test/VoiClaReq");
        if (user != null) {
            model.addAttribute("userName", user.getUsername());
        } else {
            model.addAttribute("userName", "unknown");
        }
        return "api/VoiClaReq";
    }
    // 접근 권한이 없는 사용자 튕구는 곳
    @GetMapping("/err/denied-page")
    public String accessDenied(
            @AuthenticationPrincipal User user,
            Model model){
        if (user != null) {
            model.addAttribute("userName", user.getUsername());
        } else {
            model.addAttribute("userName", "unknown");
        }
        return "err/denied";
    }

}
