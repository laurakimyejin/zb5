package com.shiromi.ashiura.controller.api;


import com.shiromi.ashiura.domain.dto.response.ResultResponseDTO;
import com.shiromi.ashiura.service.LoadingService;
import com.shiromi.ashiura.service.webClient.WebClientFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class asyncApiController {

    private final WebClientFileService webClientFileService;
    private final LoadingService loadingService;

    @Value("${url.api}")
    private String urlApi;

    //뷰에서 폼데이터를 받아서 py로 쏘는 메소드를 호출하는 맵핑
    @PostMapping("/VoiClaReq")
    public void VoiClaReq(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userName") String userName,
            @RequestParam("declaration") String declaration
    ) throws IOException {
        log.info("async: {}", Thread.currentThread().getName());
        log.info("post: {}",urlApi+"/VoiClaReq");
        log.info("data: {}/{}/{}",file.getOriginalFilename(),userName,declaration);

        webClientFileService.webCliTestMethod(file,userName,declaration);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body();
    }

    //py에서 던져진 Json데이터를 받아서 DTO객체를 set하는 메소드를 호출하는 맵핑
    @PostMapping("/progress/{userName}/{declaration}")
    public void progress(
            @RequestBody ResultResponseDTO resultRes,
            @PathVariable("userName") String userName,
            @PathVariable("declaration") String declaration
    ) throws InterruptedException {
        log.info("post: {}/{}/{}",urlApi+"/progress",userName,declaration);
        loadingService.nowLoading(resultRes,userName,declaration);
    }


    //app이 DTO객체를 get하는 메소드를 호출하는 맵핑
    @GetMapping("/VoiClaReq")
    public ResponseEntity<?> loading() throws InterruptedException {
        log.info("Get: {}", urlApi + "/api/loading");
        String result = loadingService.showLoading();
        if (result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}