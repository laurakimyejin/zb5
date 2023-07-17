package com.shiromi.ashiura.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VoiceDataDomain {

    long id;
    String userName;
    String declaration;
    String audioFile;
    String content;
    String disData;
    String createdDate;

    @Builder
    public VoiceDataDomain(long id, String userName, String declaration, String audioFile, String content, String disData, String createdDate) {
        this.id = id;
        this.userName = userName;
        this.declaration = declaration;
        this.audioFile = audioFile;
        this.content = content;
        this.disData = disData;
        this.createdDate = createdDate;
    }
    public VoiceDataDomain toEntity() {
        return VoiceDataDomain.builder()
                .id(id)
                .userName(userName)
                .declaration(declaration)
                .audioFile(audioFile)
                .content(content)
                .disData(disData)
                .createdDate(createdDate)
                .build();
    }
}

