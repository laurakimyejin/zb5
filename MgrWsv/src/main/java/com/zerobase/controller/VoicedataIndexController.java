package com.zerobase.controller;

import com.zerobase.application.service.VoicedataService;
import com.zerobase.domain.Voicedata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.zerobase.application.dto.VoicedataDto;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class VoicedataIndexController {

    @Autowired
    private final VoicedataService voicedataService;

    //초기화면
    @GetMapping("/")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable){
        Page<Voicedata> voicedata = voicedataService.list(pageable);
        model.addAttribute("voicedatas", voicedata);

        return "Index";

    }

    /*전체 목록 읽어오기*/
    @GetMapping("/voice/list")
    public String dataList(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
    Pageable pageable){
        Page<Voicedata> voicedata = voicedataService.list(pageable);
        model.addAttribute("voicedatas", voicedata);
        return "voice/datalist";

    }

    //상세보기
    @GetMapping("/voice/read/{id}")
    public String read (@PathVariable Long id, Model model) {
        VoicedataDto dto = voicedataService.findById(id);
        model.addAttribute("voicedata", dto);
        return "voice/detail";
    }

    //재학습된 목록읽어오기
    @GetMapping("/voice/retrain")
    public String reList(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
                         Pageable pageable){
        Page<Voicedata> voicedata = voicedataService.list(pageable);
        model.addAttribute("voicedatas", voicedata);
        return "voice/retrained";
    }

    //수정하기
//    @GetMapping("/voice/read/{id}")
//    public String updated(@PathVariable Long id, Model model) {
//        VoicedataDto dto = voicedataService.findById(id)
//    }




}