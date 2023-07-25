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

    /*기간별 검색*/
//        //신고된 번호별 검색
//    @Query("SELECT v FROM Voicedata v JOIN FETCH v.user WHERE v.created_date BETWEEN :start AND :end AND v.userid = :userid")
////    @Query("SELECT v FROM Voicedata v JOIN User u ON v.user = u.userid WHERE v.created_date BETWEEN :start AND :end AND u.userid = :userid")
//    Page<Voicedata> searchAllByUserId(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("userid") String userid);
//
//
//    @Query("SELECT v FROM Voicedata v WHERE v.created_date BETWEEN :start AND :end AND v.declaration = :declaration")
//    Page<Voicedata> searchAllByDecl(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("declaration") String declaration);


//    Page<Voicedata> findByDeclaration(Pageable pageable);
//    Page<Voicedata> findAllWithUser(Pageable pageable);
}
