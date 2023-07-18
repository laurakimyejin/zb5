package com.shiromi.ashiura.domain.entity;

import com.shiromi.ashiura.domain.dto.VoiceDataDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor
@Table(name = "voicedata")
@Setter
@Getter
@Entity
public class VoiceDataEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="user_id", length = 40)
    private String userName;

    @Column(name="declaration", length = 40)
    private String declaration;

    @Column(name="audio_file", length = 60)
    private String audioFile;

    @Column(name="content", columnDefinition ="BLOB")
    private String content;

    @Column(name="disdata", length = 1, columnDefinition = "CHAR")
    private String disData;

    @Column(name="created_date", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private String createdDate;

    @Column(nullable = false)
    private String persent;

    //관리자가 지정한 결과값
    @Column(nullable = true)
    private String admindata;

    //재학습된 결과 확률
    @Column(nullable = true)
    private String reroll;

    //변조
    @Column(nullable = false)
    private String mfcc;

    //재학습된(수정된)날짜
    @Column
    private LocalDate modified_date;

    @Builder
    public VoiceDataEntity(long id, String userName, String declaration,
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
    public VoiceDataDomain toDomain() {
        return VoiceDataDomain.builder()
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

