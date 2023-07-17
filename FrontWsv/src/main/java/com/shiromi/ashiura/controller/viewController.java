package com.shiromi.ashiura.controller;

import com.shiromi.ashiura.config.jwt.JwtProvider;
import com.shiromi.ashiura.domain.entity.VoiceDataEntity;
import com.shiromi.ashiura.service.LoadingService;
import com.shiromi.ashiura.service.UserService;
import com.shiromi.ashiura.service.VoiceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @CookieValue(value = "Bearer", required = false) String token,
            Model model)   {
        log.info("View: {}", urlApi + "/");
        if (token != null) {
            log.info("token: {}",token);
            String user = jwtProvider.showClaims(token);
            model.addAttribute("userName", user);
            return "home";
        }
        model.addAttribute("userName","plz login");
        return "home";
    }

    @GetMapping("/view/info")
    public String view_user_info(
            @CookieValue(value = "Bearer", required = false) String token,
            Model model) {
        log.info("View: {}", urlApi + "/view/info");
        if (token != null) {
            String user = jwtProvider.showClaims(token);
            List<VoiceDataEntity> voiceData = voiceDataService.findByUserNameAll(user);

            model.addAttribute("userName", user);
            model.addAttribute("voiceData", voiceData);

            return "view/user_info";
        }
        return "view/user_info";
    }

    @GetMapping("/view/loading")
    public String view_loading(Model model) throws InterruptedException {
        log.info("Get: {}", urlApi + "/view/loading");
        model.addAttribute("result",loadingService.showLoadingView());

        return "view/Loading";
    }

    @GetMapping("/test/addVoi")
    public String addVoiceData() {
        log.info("Get: {}", urlApi + "/test/add_voice_data");
        return "test/add_voice_data";
    }

    @GetMapping("/view/VoiClaReq")
    public String VoiceClientRequest(
            @CookieValue(value = "Bearer", required = false) String token,
            Model model
    ) {
        log.info("Get: {}", urlApi + "/Test/VoiClaReq");
        if (token != null) {
            String user = jwtProvider.showClaims(token);
            model.addAttribute("userName", user);

            return "api/VoiClaReq";
        }

        return "api/VoiClaReq";
    }

    @GetMapping("/err/denied-page")
    public String accessDenied(){
        return "err/denied";
    }

}
