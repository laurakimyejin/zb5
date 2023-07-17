package com.zerobase.domain;

import com.zerobase.application.dto.VoicedataDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Voicedata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false)
    private String audio_file;

    @Lob
    private String content;

    @Column(nullable = false)
    private String declaration;

    //판별결과
    @Column(nullable = false)
    private String disdata;

    //판별결과 확률
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

    @Column
    private LocalDate created_date;

    //재학습된(수정된)날짜
    @Column
    private LocalDate modified_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public VoicedataDto toDto(){
        return VoicedataDto.builder()
                .id(id)
                .audio_file(audio_file)
                .content(content)
                .declaration(declaration)
                .disdata(disdata)
                .persent(persent)
                .admindata(admindata)
                .reroll(reroll)
                .mfcc(mfcc)
                .created_date(created_date)
                .modified_date(modified_date)
                .user(user)
                .build();
    }

//    public void update(String disdata){
//        this.disdata = disdata;
//    }
}
