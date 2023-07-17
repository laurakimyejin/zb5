package com.shiromi.ashiura.domain.dto.response;


import lombok.*;


@NoArgsConstructor
@Getter
@Setter
public class ResultResponseDTO {

    private String userName;
    private String declaration;
    private String progress;
    private String voiceResult;
    private String mfccResult;

    @Builder
    public ResultResponseDTO(String userName, String declaration, String progress, String voiceResult, String mfccResult) {
        this.userName = userName;
        this.declaration = declaration;
        this.progress = progress;
        this.voiceResult = voiceResult;
        this.mfccResult = mfccResult;
    }
}
