package com.shiromi.ashiura.repository;

import com.shiromi.ashiura.domain.entity.VoiceDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface VoiceDataRepository extends JpaRepository<VoiceDataEntity,Long> {

//    Optional<VoiceDataEntity> findByUserName(String userName);
    List<VoiceDataEntity> findByUserNameOrderByCreatedDateDesc(String userName);

    List<VoiceDataEntity> findTop10ByUserNameOrderByCreatedDateDesc(String userName);

    Optional<VoiceDataEntity> findByDeclaration(String declaration);
}

