package com.zerobase.application.service;

import javax.transaction.Transactional;

import com.zerobase.application.dto.VoicedataDto;
import com.zerobase.application.dto.request.UserUpdateDto;
import com.zerobase.domain.Voicedata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerobase.application.dto.request.UserSaveRequestDto;
import com.zerobase.domain.RoleType;
import com.zerobase.domain.User;
import com.zerobase.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.zerobase.domain.RoleType.ADMIN;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder encoder;

    //회원 가입 DB 저장
    @Transactional
    public void join(UserSaveRequestDto userSaveRequestDto) {
        if(userSaveRequestDto.getUsername().equals("admin")) {
            userSaveRequestDto.setRole(RoleType.ADMIN);
        }
        String encodePassword = encoder.encode(userSaveRequestDto.getPassword());
        userSaveRequestDto.setPassword(encodePassword);
        User user = userSaveRequestDto.toEntity();
        userRepository.save(user);
    }

    //회원 정보 DB 업데이트
    @Transactional
    public void update(User user) {
        User findUser = userRepository.findById(user.getIdx()).orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        if (findUser != null) {
            findUser.updatePhoneNumber(user.getPhoneNumber());
            findUser.updatePassword(user.getPassword());
            findUser.updateRole(user.getRole());

            userRepository.save(findUser);

            //서비스 단이라서 다시 저장해야할 것 같은데 github에는 안되어 있는데? 밑에 findUser 메소드도 쓰지 않았음.
        }
    }

    //updaterole
//    @Transactional
//    public void updateRole(Long idx, UserUpdateDto userUpdateDto){
//        log.info("3");
//        User user = userRepository.findById(idx).orElseThrow(()->
//                new IllegalArgumentException("데이터가 존재하지 않습니다. idx=" + idx));
//        log.info("4");
//
//        user.updateRole(userUpdateDto.toEntity().getRole());
//    }

    @Transactional
    public Page<User> list(Pageable pageable){
        return userRepository.findAllWithUser(pageable);
    }
}
