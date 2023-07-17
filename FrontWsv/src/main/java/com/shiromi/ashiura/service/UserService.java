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
//        UserEntity user = UserEntity.builder()
//                .userName(userDomain.getUserName())
//                .password(BCrypt.hashpw(
//                        userDomain.getPassword(), BCrypt.gensalt()))
//                .phoneNumber(userDomain.getPhoneNumber())
//                .rating("N")
//                .build();
//        UserEntity savedUser = userRepository.save(user);

        UserEntity savedUser = userRepository.save(userDomain.toEntity());
        return savedUser.toString();
    }

//    public String userSave(Model model) {
//        log.info("uNam: {}", model.getAttribute("userName"));
//        log.info("pass: {}", model.getAttribute("password"));
//        log.info("phNu: {}", model.getAttribute("phoneNumber"));
//        UserEntity user = UserEntity.builder()
//                .userName((String) model.getAttribute("userName"))
//                .password((String) model.getAttribute("password"))
//                .phoneNumber((String) model.getAttribute("phoneNumber"))
//                .rating("N")
//                .build();
//        UserEntity savedUser = userRepository.save(user);
//        return savedUser.toString();
//    }

//    public String userSave(String userName, String password, String phoneNumber) {
//        log.info("uNam: {}", userName);
//        log.info("pass: {}", password);
//        log.info("phNu: {}", phoneNumber);
//        UserEntity user = UserEntity.builder()
//                .userName(userName)
//                .password(password)
//                .phoneNumber(phoneNumber)
//                .rating("N")
//                .build();
//        UserEntity savedUser = userRepository.save(user);
//        return savedUser.toString();
//    }

    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(IllegalAccessError::new);
//                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public List<UserEntity> findByAll() {
        return userRepository.findAll();
    }

//    public UserDomain userLogin(UserLoginRequest userLoginRequest) {
//
//        UserEntity user = findByUserName(userLoginRequest.getUserName());
//        if (!user.getPassword().matches(userLoginRequest.getPassword())) {
//            log.info("PsMa :{}","password dose not match");
//            return new UserDomain();
//        }
//        log.info("PsMa :{}","password is match");
//        return user.toDomain();
//    }
}
