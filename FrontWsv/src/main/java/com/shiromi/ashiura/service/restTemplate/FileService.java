package com.shiromi.ashiura.service.restTemplate;

import com.shiromi.ashiura.domain.entity.FileEntity;
import com.shiromi.ashiura.domain.dto.response.ResultResponseDTO;
import com.shiromi.ashiura.repository.FileRepository;
import com.shiromi.ashiura.service.utils.UriUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

//@Deprecated
@RequiredArgsConstructor
@Slf4j
@Service
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${url.py}")
    private String uriPy;

    private final FileRepository fileRepository;
    private ResultResponseDTO resultDTO;
    private UriUtil uriUtil;

    public ResponseEntity<?> saveFile(MultipartFile file,String userName, String declaration) throws IOException {
        log.info("{}", file);
        if (file.isEmpty()) {
            return new ResponseEntity<>("file doesn't exist", HttpStatus.BAD_REQUEST);
        }

        String origName = file.getOriginalFilename();
        String savedName = UUID.randomUUID() + origName.substring(origName.lastIndexOf("."));
        String savedPath = fileDir + savedName;

        file.transferTo(new File(savedPath));
        fileRepository.save(FileEntity.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build());

        URI uri = uriUtil.uriPy("/api/VoiClaReq", userName, declaration);
        log.info("uri :{}", uri);

        MultiValueMap<String, String> MVMap = new LinkedMultiValueMap<>();
        MVMap.add("file", savedPath);

        RequestEntity<?> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(MVMap);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(requestEntity, String.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


//        MultiValueMap<String, String> MVMap = new LinkedMultiValueMap<>();
//        MVMap.add("file", savedPath);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        RequestEntity<MultiValueMap<String, String>> requestEntity =
//                new RequestEntity<>(MVMap, headers, HttpMethod.POST, uri);
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(requestEntity,String.class);
//
//        return response.getBody();




//    public void runPy(String userName, String declaration) throws InterruptedException {
//
//        resultDTO.setUserName(userName);
//        resultDTO.setDeclaration(declaration);
//
//        for (int i=0;i<=10;i++){
//            Thread.sleep(1000);
//            resultDTO.setProgress(String.valueOf(i));
//            if (i==10) {
//                resultDTO.setResult(String.valueOf((int) (Math.random()*2)));
//            }
//        }
//    }


    public ResponseEntity<?> runPy(String userName, String declaration, ResultResponseDTO resultRes) {
        resultRes.setUserName(userName);
        resultRes.setDeclaration(declaration);
        resultDTO = resultRes;
        log.info("progress: {},{},{}",
                resultDTO.getProgress(),resultDTO.getVoiceResult(),resultDTO.getMfccResult());
//        log.info("progress: {},{}",
//                resultDTO.getProgress(),resultDTO.getResult());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResultResponseDTO checkFile(
            String userName, String declaration
    ) throws InterruptedException {

        if (resultDTO.getUserName().equals(userName) && resultDTO.getDeclaration().equals(declaration)) {
            if (resultDTO.getProgress() != null) {
                log.info("view: {}%",resultDTO.getProgress());
                return resultDTO;
            }
            log.info("progress is null");
            Thread.sleep(3000);
            return null;
        }
        log.info("userName,declaration is mismatch");
        return null;


    }
}
