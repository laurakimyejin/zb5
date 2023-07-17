package com.shiromi.ashiura.repository;

import com.shiromi.ashiura.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUserName(String userName);
}

