package com.zerobase.application.service;

import com.zerobase.application.dto.VoicedataDto;
import com.zerobase.domain.Voicedata;
import com.zerobase.domain.User;
import com.zerobase.infrastructure.repository.UserRepository;
import com.zerobase.infrastructure.repository.VoicedataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoicedataService {

    private final VoicedataRepository voicedataRepository;
//    private final UserRepository userRepository;

    //create

    //페이징 처리한 전체목록
    @Transactional
    public Page<Voicedata> list(Pageable pageable){
        return voicedataRepository.findAllWithUser(pageable);
    }

    //페이징 처리x 전체 목록
    @Transactional
    public List<VoicedataDto> findAll(){
        return voicedataRepository.findAll().stream().map(new VoicedataDto()::toDto).collect(Collectors.toList());

    }

    //id 상세
    @Transactional
    public VoicedataDto details(Long id){
        Voicedata voicedata = voicedataRepository.findByIdWithUser(id);
        return voicedata.toDto();
    }

    //Dto상세
    @Transactional
    public VoicedataDto findById(Long id){
        Voicedata voicedata = voicedataRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다 id= " + id));
        return voicedata.toDto();
    }
    //read list
//    @Transactional(readOnly = true)
//    public VoicedataDto.Response findById(Long id){
//        Voicedata voicedata = voicedataRepository.findById(id).orElseThrow(()->
//                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));
//
//        return new VoicedataDto.Response(voicedata);
//    }

    //update
//    @Transactional
//    public void update(Long id, VoicedataDto.Request dto){
//        Voicedata voicedata = voicedataRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("rrrr id=" + id));
//
//        voicedata.update(dto.getDisdata());
//    }

    //delete

    //viewcount



    //search
//    @Transactional(readOnly = true)
//    public Page<Voicedata> search(String keyword, Pageable pageable){
//        Page<Voicedata> voicedataList = voicedataRepository.findByDeclaration(keyword, pageable);
//        return voicedataList;
//    }

}
