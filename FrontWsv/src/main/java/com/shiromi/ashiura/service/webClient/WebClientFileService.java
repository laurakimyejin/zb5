package com.shiromi.ashiura.service.webClient;


import com.fasterxml.jackson.databind.JsonNode;
import com.shiromi.ashiura.domain.entity.FileEntity;
import com.shiromi.ashiura.repository.FileRepository;
import com.shiromi.ashiura.service.utils.UriUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WebClientFileService {

    private final FileRepository fileRepository;
    private final UriUtil uriUtil;

    @Value("${file.dir}")
    private String fileDir;

    public void byteReceive(
            MultipartFile file,
            String userName,
            String declaration) throws IOException {

        URI uri = uriUtil.uriPy("/api/VoiClaReq",userName,declaration);

//        log.info("post:{}{}/{}/{}", urlPy,path,userName,declaration);
        log.info("post: {}", uri);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());


        WebClient.create().post()
                .uri(uri)
//                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
//                .exchangeToMono()
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> log.error("Err :", e));

//                .toEntity(Void.class)
//                .doOnError(e -> log.error("Err :",e));
//                .subscribe();
//                .block();


    }


    public void webCliTestMethod(MultipartFile file, String userName, String declaration) throws IOException {
        log.info("in service: {}",file);
        if (file.isEmpty()) {
            return;
//            return new ResponseEntity<>("file doesn't exist", HttpStatus.BAD_REQUEST);
        }
        //원본 파일명 추출
        String origName = file.getOriginalFilename();
        //UUID 생성후 원본에서 확장자 추출한뒤 결합
        String savedName = UUID.randomUUID() + origName.substring(origName.lastIndexOf("."));
        //파일을 저장할 경로 생성
        String savedPath = fileDir + savedName;
        //로컬에 UUID를 파일명으로 저장
        file.transferTo(new File(savedPath));
        //엔티티 생성후 저장
        fileRepository.save(FileEntity.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build());
        URI uri = uriUtil.uriPy("/api/VoiClaReq",userName,declaration);
        log.info("uri :{}",uri);

        MultiValueMap<String, String> MVMap = new LinkedMultiValueMap<>();
        MVMap.add("file", savedPath);

//        Mono<HttpStatus> httpStatusMono = WebClient.create().post()
//                .uri(uri)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(MVMap))
//                .exchangeToMono(response -> {
//                    if (response.statusCode().equals(HttpStatus.OK)){
//                        return response.bodyToMono(HttpStatus.class).thenReturn(response.statusCode());
//                    } else {
//                        throw new ServiceException("Error uploading file");
//                    }
//                });

        WebClient.create().post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(MVMap))
                .retrieve()
                .toEntity(String.class)
                .subscribe();
        log.info("VoiClaReq post success");

//                .exchangeToMono()
//                .retrieve();
//                .exchangeToMono();
//                .bodyToMono(String.class);
//                .toEntity(JsonNode.class)
//                .doOnError(e -> log.error("MaEr :",e))
//                .subscribe();
//                .block();

    }

}

//        Mono<HttpStatus> httpStatusMono = webClient.post()
//                .uri(urlPy)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromResource(file.getResource()))
//                .exchangeToMono(response -> {
//                    if (response.statusCode().equals(HttpStatus.OK)){
//                        return
//                                response.bodyToMono(HttpStatus.class).thenReturn(response.statusCode());
//                    } else {
//                        throw new ServiceException("Error uploading file");
//                    }
//                });
//        log.info("{}",httpStatusMono);

//        webClient.post().uri(uri)
//                .bodyValue(file.getBytes())
//                .retrieve()
//                .toEntity(byte[].class)
//                .subscribe();
//                .block();

//        for (int i=0;i<11;i++) {
//            Thread.sleep(1000);
//            webClient.get().uri(uri)
//                    .retrieve()
//                    .toEntity(String.class)
//                    .subscribe();
//            log.info("resp:{}",i);
//        }


    //        RequestEntity<byte[]> requestEntity = RequestEntity
//                .post(uri)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("","")
//                .body(file.getBytes());
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<byte[]> fileResponse = restTemplate.exchange(requestEntity, byte[].class);
//        log.info("resf:{}",fileResponse);



//        WebClient webClient = WebClient.create();
//        Mono<?> fileResponse = webClient
//                .post()
//                .uri(uri)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData("file", file))
//                .retrieve()
//                .bodyToMono(ResponseEntity.class);
//        fileResponse.subscribe(
//                r -> log.info("resf:{}", r),
//                e -> log.error("error:{}", e)
//        );



//    public Void exchange(
//            MultipartFile file,
//            String userName,
//            String declaration
//    ) throws IOException, InterruptedException {
//        URI uri = UriComponentsBuilder
//                .fromUriString(urlSer)
//                .path("/api/VoiClaReq/{userName}/{declaration}")
//                .encode()
//                .build()
//                .expand(userName,declaration)
//                .toUri();
//        log.info("post:{}",uri);
//
//        RequestEntity<?> requestEntity = RequestEntity
//                .post(uri)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(file);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<?> fileResponse = restTemplate.exchange(requestEntity, MultipartFile.class);
//        log.info("resf:{}",fileResponse);
// }


//        RequestEntity<byte[]> requestEntity = RequestEntity
//                .post(uri)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("","")
//                .body(file.getBytes());
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<byte[]> fileResponse = restTemplate.exchange(requestEntity, byte[].class);
//        log.info("resf:{}",fileResponse);

//        for (int i=0;i<101;i++) {
//            Thread.sleep(1000);
//            RequestEntity<Void> request = RequestEntity
//                    .get(uri)
//                    .header("", "")
//                    .build();
//            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
//            log.info("resp:{}",String.valueOf(i) + response);
//        }
//        return null;



