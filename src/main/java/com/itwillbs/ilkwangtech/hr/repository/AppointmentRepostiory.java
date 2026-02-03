package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.entity.AppointmentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AppointmentRepostiory extends JpaRepository<AppointmentEntity, Long> {

    @Query("SELECT a FROM AppointmentEntity a WHERE a.draft.draftId = :draftId")
    Optional<AppointmentEntity> findByDraftId(@Param("draftId") Long draftId);

    // 발령 리스트 전체 조회 or 검색 조건 조회(이름, 사번)
    @Query("SELECT a FROM AppointmentEntity a " +
            "WHERE (:keyword IS NULL OR :keyword = '' " +
            "OR a.memberId.employeeNumber LIKE %:keyword% " +
            "OR a.memberId.name LIKE %:keyword%)")
    Page<AppointmentEntity> findAllBySearch(Pageable pageable, @Param("keyword") String keyword);

}
