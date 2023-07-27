package com.zerobase.infrastructure.repository;

import com.zerobase.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserid(String userid);

    @Query(value = "SELECT v FROM User v ",
            countQuery = "SELECT COUNT(v) FROM User v")
    Page<User> findAllWithUser(Pageable pageable);


}
