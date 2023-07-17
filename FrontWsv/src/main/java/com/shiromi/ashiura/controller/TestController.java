package com.shiromi.ashiura.controller;

import com.shiromi.ashiura.domain.dto.VoiceDataDomain;
import com.shiromi.ashiura.service.VoiceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class TestController {

    private final VoiceDataService voiceDataService;


    @Value("${url.api}")
    private String urlApi;

    //신고 내용을 보여주기 위해 통화테이블의 데이터를 임시로 추가하는 테스트용 API
    @PostMapping("/voice_data/add")
    public ResponseEntity<?> save(VoiceDataDomain voiceDataDomain) {
        log.info("data:{}", voiceDataDomain.toString());
        String voiceData = voiceDataService.voiceDataSave(voiceDataDomain);
        return ResponseEntity.status(HttpStatus.OK)
                .body(voiceData);
    }

}
