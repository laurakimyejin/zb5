package com.shiromi.ashiura.controller.api;

import com.shiromi.ashiura.domain.dto.response.ResultResponseDTO;
import com.shiromi.ashiura.service.restTemplate.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Deprecated
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/api")
//@RestController
public class syncApiController {
//
//    private final FileService fileService;
//
//    //뷰에서 폼데이터를 받아서 py로 쏘는 메소드를 호출하는 맵핑
//    @PostMapping("/VoiClaReq")
//    public ResponseEntity<?> sendVoice(
//            @RequestAttribute("file") MultipartFile file,
//            @RequestParam("userName") String userName,
//            @RequestParam("declaration") String declaration
//    ) throws IOException {
//        log.info("post/SyncApi/VoiClaReq");
//        log.info("SendVoice: {}/{}/{}", file, userName, declaration);
//        ResponseEntity<?> response = fileService.saveFile(file, userName, declaration);
//        log.info("fileServiceIsDone: {}", response);
////    fileService.runPy(userName,declaration);
//        return response;
//    }
//
//    //py에서 Json데이터를 받아서 DTO객체를 set하는 메소드를 호출하는 맵핑
//    @PostMapping("/progress/{userName}/{declaration}")
//    public ResponseEntity<?> receiveResult(
//            @RequestBody ResultResponseDTO resultRes,
//            @PathVariable("userName") String userName,
//            @PathVariable("declaration") String declaration) {
//        log.info("post/SyncApi/progress");
//        log.info("data: {}/{}/{}/{}",userName,declaration, resultRes.getProgress(),resultRes.getResult());
//        ResponseEntity<?> response = fileService.runPy(userName, declaration, resultRes);
//        log.info("res: {}", response);
//        return response;
//    }
//
//    //DTO객체를 get하는 메소드를 호출하는 맵핑
//    @GetMapping("/progress/{userName}/{declaration}")
//    public ResponseEntity<?> responseResult(
//            @PathVariable("userName") String userName,
//            @PathVariable("declaration") String declaration) throws InterruptedException {
//        log.info("get/SyncApi/progress");
//        log.info("{},{}", userName, declaration);
//        ResultResponseDTO responseDTO = fileService.checkFile(userName, declaration);
//        if (responseDTO != null) {
//            log.info("return DTO");
//            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//        } else {
//            log.info("return Http OK");
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//    }
//

}
