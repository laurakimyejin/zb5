package com.shiromi.ashiura.service;

import com.shiromi.ashiura.repository.VoiceDataRepository;
import com.shiromi.ashiura.domain.dto.VoiceDataDomain;
import com.shiromi.ashiura.domain.entity.VoiceDataEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoiceDataService {

    private final VoiceDataRepository voiceDataRepository;

    public String voiceDataSave(VoiceDataDomain voiceDataDomain){
            log.info(voiceDataDomain.toString());
            VoiceDataEntity voiceData = VoiceDataEntity.builder()
                            .id(voiceDataDomain.getId())
                            .userName(voiceDataDomain.getUserName())
                            .declaration(voiceDataDomain.getDeclaration())
                            .audioFile(voiceDataDomain.getAudioFile())
                            .content(voiceDataDomain.getContent())
                            .disData(voiceDataDomain.getDisData())
                            .createdDate(null)
                            .build();
        VoiceDataEntity savedVoiceData = voiceDataRepository.save(voiceData);
        log.info(savedVoiceData.toString());

//        VoiceDataEntity savedVoiceData = voiceDataRepository.save(voiceDataDomain.toEntity());
        return savedVoiceData.toString();
    }

    public List<VoiceDataEntity> findByUserNameAll(String userName) {
        return voiceDataRepository.findTop10ByUserNameOrderByCreatedDateDesc(userName);
    }


}
