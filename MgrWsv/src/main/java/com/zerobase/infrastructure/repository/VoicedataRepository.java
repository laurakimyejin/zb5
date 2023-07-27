package com.zerobase.infrastructure.repository;

import com.zerobase.domain.Voicedata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoicedataRepository extends JpaRepository<Voicedata, Long> {



//    Page<Voicedata> findByKeywordAndType(String keyword, String type);

    @Query(value = "select v from Voicedata v where v.declaration = :declaration")
    Page<Voicedata> findByDeclaration(String declaration, Pageable pageable);

//    @Query(value = "select v from Voicedata v INNER JOIN User u ON v.idx=idx")
//    Page<Voicedata> findByIdWithUser(String userid, Pageable pageable);

    @Query(value = "select v from Voicedata v INNER JOIN User u ON v.id = :userid")
    Page<Voicedata> findByUserId(String userid, Pageable pageable);

    @Query(value = "SELECT v FROM Voicedata v JOIN FETCH v.user WHERE v.id = :id")
    Voicedata findByIdWithUser(@Param("id") long id);


    @Query(value = "SELECT v FROM Voicedata v JOIN FETCH v.user",
            countQuery = "SELECT COUNT(v) FROM Voicedata v JOIN v.user")
    Page<Voicedata> findAllWithUser(Pageable pageable);

//    Page<Voicedata> findByTitleContaining(Pageable pageable);

//
//    Page<Voicedata> getSearchVoicePage(VoicedataSearchDto voicedataSearchDto, Pageable pageable);
//
//    Page<Voicedata> getSearchVoicePage(VoicedataSearchDto voicedataSearchDto, Pageable pageable);

//    Page<Voicedata> searchAllBy(String startDate, String endDate, Pageable pageable);

    /*기간별 검색*/


//    /*기간->신고 번호별 검색*/    @Query("SELECT v from Voicedata v WHERE v.created_date BETWEEN :start AND :end AND v.disdata = :disdata")
//    @Query("SELECT v FROM Voicedata v JOIN FETCH v.user WHERE v.created_date BETWEEN :start AND :end AND v.userid = :userid")
//    @Query("SELECT v FROM Voicedata v JOIN User u ON v.user = u.userid WHERE v.created_date BETWEEN :start AND :end AND u.userid = :userid")
//    Page<Voicedata> searchAllByUserId(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("disdata") String disdata);

//    @Query("SELECT v FROM Voicedata v WHERE v.created_date BETWEEN :start AND :end AND v.declaration = :declaration")
//    Page<Voicedata> searchAllByDecl(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("declaration") String declaration);


//    Page<Voicedata> findByDeclaration(Pageable pageable);
//    Page<Voicedata> findAllWithUser(Pageable pageable);
}
