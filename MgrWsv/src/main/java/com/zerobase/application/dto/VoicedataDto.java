package com.zerobase.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.domain.User;
import com.zerobase.domain.Voicedata;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoicedataDto {

    private Long id;
    private String audio_file;
    private String content;
    private String declaration;
    private String disdata;//판별
    private String persent;//판별값확률
    private String admindata;
    private String reroll;//재학습된거 확률
    private String mfcc;//변조
    private LocalDate created_date;
    private LocalDate modified_date;
    private User user;

    //검색용
    private String type;
    private String keyword;

    /*Dto->entity*/
    public Voicedata toEntity(Long idx){
        return Voicedata.builder()
                .id(id)
                .audio_file(audio_file)
                .content(content)
                .declaration(declaration)
                .disdata(disdata)
                .persent(persent)
                .admindata(admindata)
                .reroll(reroll)
                .mfcc(mfcc)
                .user(user)
                .build();

//            return voicedata;

    }

//    //보이스 게시물 정보 응답 할 response class
//    데이터를 dto로 변환하여 응답
//    별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간 무한참조 방지

//    @Getter
//    public static class Response{
//        private final Long id;
//        private final String audio_file;
//        private final String content;
//        private final String declaration;
//        private final String disdata;//판별
//        private final String persent;
//        private final String admindata;
//        private final String reroll;//재학습
//        private final String mfcc;//변조
//        private final LocalDate created_date;
//        private final LocalDate modified_date;
//        private final Long userIdx;

    //Entity->Dto
    public VoicedataDto toDto(Voicedata voicedata){
        this.id=voicedata.getId();
        this.audio_file=voicedata.getAudio_file();
        this.content= voicedata.getContent();
        this.declaration=voicedata.getDeclaration();
        this.disdata=voicedata.getDisdata();
        this.persent=voicedata.getPersent();
        this.admindata=voicedata.getAdmindata();
        this.reroll=voicedata.getReroll();
        this.mfcc=voicedata.getMfcc();
        this.created_date=voicedata.getCreated_date();
        this.modified_date=voicedata.getModified_date();
        this.user=voicedata.getUser();
        return this;
    }

//    @QueryProjection
//    public VoicedataDto(Long id,String audio_file,String content, String declaration ,String disdata ,String persent ,String admindata ,String reroll ,String mfcc, LocalDate created_date,LocalDate modified_date ,User user){
//        this.id=id;
//        this.audio_file=audio_file;
//        this.content= content;
//        this.declaration=declaration;
//        this.disdata=disdata;
//        this.persent=persent;
//        this.admindata=admindata;
//        this.reroll=reroll;
//        this.mfcc=mfcc;
//        this.created_date=created_date;
//        this.modified_date=modified_date;
//        this.user=user;
//    }



}