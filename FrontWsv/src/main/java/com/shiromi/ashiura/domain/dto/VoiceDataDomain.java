package com.shiromi.ashiura.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    String persent;
    String admindata;
    String reroll;
    String mfcc;
    LocalDate modified_date;

    @Builder
    public VoiceDataDomain(long id, String userName, String declaration,
                           String audioFile, String content, String disData,
                           String createdDate, String persent,String admindata,
                           String reroll, String mfcc, LocalDate modified_date) {
        this.id = id;
        this.userName = userName;
        this.declaration = declaration;
        this.audioFile = audioFile;
        this.content = content;
        this.disData = disData;
        this.createdDate = createdDate;
        this.persent = persent;
        this.admindata = admindata;;
        this.reroll = reroll;
        this.mfcc = mfcc;
        this.modified_date = modified_date;
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
                .persent(persent)
                .admindata(admindata)
                .reroll(reroll)
                .mfcc(mfcc)
                .modified_date(modified_date)
                .build();
    }
}

