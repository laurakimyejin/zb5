package com.shiromi.ashiura.domain.entity;

import com.shiromi.ashiura.domain.dto.VoiceDataDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


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

    @Builder

    public VoiceDataEntity(Long id, String userName, String declaration, String audioFile, String content, String disData, String createdDate) {
        this.id = id;
        this.userName = userName;
        this.declaration = declaration;
        this.audioFile = audioFile;
        this.content = content;
        this.disData = disData;
        this.createdDate = createdDate;
    }
    public VoiceDataDomain toDomain() {
        return VoiceDataDomain.builder()
                .userName(userName)
                .declaration(declaration)
                .audioFile(audioFile)
                .content(content)
                .disData(disData)
                .createdDate(createdDate)
                .build();
    }
}

