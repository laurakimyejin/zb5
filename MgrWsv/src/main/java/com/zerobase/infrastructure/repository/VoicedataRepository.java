package com.zerobase.infrastructure.repository;

import com.zerobase.domain.Voicedata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoicedataRepository extends JpaRepository<Voicedata, Long> {

    @Query(value = "SELECT v FROM Voicedata v JOIN FETCH v.user WHERE v.id = :id")
    Voicedata findByIdWithUser(@Param("id") long id);


    @Query(value = "SELECT v FROM Voicedata v JOIN FETCH v.user",
            countQuery = "SELECT COUNT(v) FROM Voicedata v JOIN v.user")
    Page<Voicedata> findAllWithUser(Pageable pageable);

//    Page<Voicedata> findByDeclaration(Pageable pageable);
//    Page<Voicedata> findAllWithUser(Pageable pageable);
}
