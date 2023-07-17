package com.shiromi.ashiura.service;

import antlr.Token;
import com.shiromi.ashiura.config.jwt.JwtProvider;
import com.shiromi.ashiura.domain.dto.TokenInfo;
import com.shiromi.ashiura.repository.UserRepository;
import com.shiromi.ashiura.domain.dto.UserDomain;
import com.shiromi.ashiura.domain.dto.request.UserLoginRequest;
import com.shiromi.ashiura.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenInfo userLogin(UserLoginRequest userLogin) {
        //1. Login ID/PW 를 기반으로 Authentication 객체 생성
        //이때 authentication는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLogin.getUserName(), userLogin.getPassword());
        //2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        //aeuthenticate 메서드가 실행될 때 CustomUserDetailsServic에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

        return tokenInfo;
    }

    //유저를 DB에 저장
    public String userSave(UserDomain userDomain) {
        if (userDomain.getRating() == null) {
            userDomain.setRating("N");
        }// Rating이 설정되지 않았을 경우 디폴트값을 주듯이 "N"으로 set

        UserEntity savedUser = userRepository.save(userDomain.toEntity());
        return savedUser.toString();
    }

    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(IllegalAccessError::new);
    }

//    public List<UserEntity> findByAll() {
//        return userRepository.findAll();
//    }

}
