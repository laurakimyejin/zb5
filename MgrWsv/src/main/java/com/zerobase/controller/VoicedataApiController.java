package com.zerobase.controller;

import com.zerobase.application.service.VoicedataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class VoicedataApiController {

    @Autowired
    private final VoicedataService voicedataService;

    /* READ */
//    @GetMapping("/voice/{id}")
//    public ResponseEntity read(@PathVariable Long id) {
//        return ResponseEntity.ok(voicedataService.findById(id));
//    }




    @GetMapping("/api/voice/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        return ResponseEntity.ok(voicedataService.findById(id));
    }
    @GetMapping("/api/voicedatas")
    public ResponseEntity findAll() {
        return ResponseEntity.ok(voicedataService.findAll());
    }






}
