package com.zerobase.controller;

import com.zerobase.application.service.VoicedataService;
import com.zerobase.domain.Voicedata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.zerobase.application.dto.VoicedataDto;

@Slf4j
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

    //일반 상세보기
    @GetMapping("/voice/read/{id}")
    public String read (@PathVariable Long id, Model model) {
        VoicedataDto dto = voicedataService.findById(id);
        model.addAttribute("voicedata", dto);
        return "voice/detail";
    }


    /*검색*/
//    @GetMapping("/voice/search")
//    public String search(Model model,
//                         @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
//        Page<Voicedata> voicedatas = voicedataService.list(pageable);
//        model.addAttribute("voicedatas", voicedatas);
//
//        return "voice/voiceSearch";
//    }
//    @GetMapping("/voice/search")
//    public String search(Model model) {
//        log.info("c1");
//        model.addAttribute("types", Arrays.asList("userid", "content"));
//        log.info("c2");
//        return "voice/search";
//
//    }
//    @PostMapping("/voice/search")
//    public String search(Model model, String keyword, String type, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
//    Pageable pageable) {
//        log.info("c3");
//        Page<Voicedata> voicedata = voicedataService.findByKeywordAndType(keyword, type);
//        log.info("c4");
//        model.addAttribute("voicedatas", voicedata);
//        log.info("c5");
//        return "voice/list";
//
//    }


//    @GetMapping("/voice/search")
//    public String search(String type, String keyword, Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
//    Pageable pageable) {
//        log.info("c1");
//        Page<Voicedata> searchList = voicedataService.search(new VoicedataDto(), pageable);
//        log.info("c2");
//
//        model.addAttribute("searchList", searchList);
//        log.info("c3");
//        model.addAttribute("keyword", keyword);
//        log.info("c4");
//
//        return "voice/voiceSearch";
//    }
//    @GetMapping("/voice/search")
//    @ResponseBody
//    private Page<VoicedataDto> search(@RequestParam("type") String type, @RequestParam("keyword") String keyword, Model model){
//
//        VoicedataDto voicedataDto = new VoicedataDto();
//        voicedataDto.setType(type);
//        voicedataDto.setKeyword(keyword);
//        return voicedataService.search(voicedataDto);
//    }
//    @GetMapping("voice/search")
//    public String search(String keyword, Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
//                         Pageable pageable){
//        Page<Voicedata> searchList = voicedataService.search(keyword, pageable);
//
//        model.addAttribute("searchList", searchList);
//        model.addAttribute("keyword", keyword);
//
//        return "voice/voiceSearch";
//    }

    //재학습된 목록읽어오기
    @GetMapping("/voice/retrain")
    public String reList(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
                         Pageable pageable){
        Page<Voicedata> voicedata = voicedataService.list(pageable);
        model.addAttribute("voicedatas", voicedata);
        return "voice/retrained";
    }

    //재학습 상세보기
    @GetMapping("/voice/retrain/{id}")
    public String reListDetail (@PathVariable Long id, Model model) {
        VoicedataDto dto = voicedataService.findById(id);
        model.addAttribute("voicedata", dto);
        return "voice/retrained_detail";
    }

//    //기간별 검색
//    @PostMapping("/voice/search")
//    public String search(@Valid @ModelAttribute("voiceSearchDto") voiceSearchDto dto,
//                         @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model){
//
////        setSearchVars(dto, pageable, model);
////        return "voice/voiceSearch";
//        Page<Voicedata> voicedata = voicedataService.searchByPageAdmin(pageable, dto);
//        model.addAttribute("vo", voicedata);
//        return "voice/voiceSearch";
//    }
//    private void setSearchVars(voiceSearchDto dto, Pageable pageable, Model model) {
//        searchStorage.setValue("search", dto);
//        Page<EstimateDetailDto> eList = estimateService.searchByPageAdmin(pageable, dto);
//        setPagingModels(model, pageable, eList, dto);
//    }

//    @GetMapping("/voice/list")
//    public String SearchForm(Model model,
//                             @RequestParam(value = "startDate", required = false) String startDate,
//                             @RequestParam(value = "endDate", required = false) String endDate
//                             ,@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
//    Pageable pageable) {
//        Page<Voicedata> voicedata = voicedataService.searchlist(pageable, startDate, endDate);
//        model.addAttribute("voicedatas", voicedata);
//
//        return "datalist::#voicelist";
//    }

    //수정하기
//    @GetMapping("/voice/read/{id}")
//    public String updated(@PathVariable Long id, Model model) {
//        VoicedataDto dto = voicedataService.findById(id)
//    }




}