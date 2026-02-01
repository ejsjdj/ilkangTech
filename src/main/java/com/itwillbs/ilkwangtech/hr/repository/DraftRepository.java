package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.hr.dto.LeaveByDepartmentDTO;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;

import com.itwillbs.ilkwangtech.hr.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DraftRepository extends JpaRepository<DraftEntity, Long> {


    @Query(
            "SELECT new com.itwillbs.ilkwangtech.hr.dto.LeaveByDepartmentDTO(" +
                    " m.name, " +
                    " d.draftStartDate, " +
                    " d.draftEndDate, " +
                    " d.draftStatus, " +
                    " d.draftTotalDate " +
                    ") " +                          // ← 공백 중요
                    "FROM DraftEntity d " +          // ← 공백
                    "JOIN d.member m " +             // ← 공백
                    "WHERE m.department = :deptCode " +
                    "AND d.draftType = 'PTO' " +
                    "AND d.draftStatus IN ('승인', '대기') " +
                    "AND d.draftStartDate <= :endDate " +
                    "AND d.draftEndDate >= :startDate " +
                    "ORDER BY d.draftStartDate DESC"
    )
    List<LeaveByDepartmentDTO> findLeaveSummaryByDept(
            @Param("deptCode") Integer deptCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    // 내가 등록한 결재 + 내가 결재자인 문서 등록
    @Query("""
    SELECT DISTINCT new com.itwillbs.ilkwangtech.hr.dto.DraftDTO(
        d.draftId,
        d.draftTitle,
        d.draftStartDate,
        d.draftEndDate,
        d.draftStatus
    )
    FROM DraftEntity d
    LEFT JOIN DraftApproveStatusEntity r ON d.draftId = r.draftEntity.draftId
    WHERE d.member.id = :userId 
       OR r.member.id = :userId
    """)
    List<DraftDTO> findAllMyDrafts(@Param("userId") Long userId);

    // 최종 상태 업데이트
    @Modifying
    @Query("""
    UPDATE DraftEntity s
    SET s.draftStatus = :finalStatus
    WHERE s.draftId = :draftId  
    """)
    void updateFinalStatus(@Param("draftId")Long draftId,
                           @Param("finalStatus") String finalStatus);


    // 휴가 상태 테이블 비교
    @Query("SELECT l FROM LeaveEntity l, DraftEntity d  " +
            "WHERE d.member.id = l.member.id " +    // 같은 사원인지 확인
            "AND l.member.id = :memberId " +         // 특정 사원 ID
            "AND d.draftId = :draftId " +           // 특정 결재 문서
            "AND d.draftStatus = '승인' " +    // 최종 승인 상태 조건 필수!
            "AND d.draftType = 'PTO'")              // 휴가 종류 확인
    Optional<LeaveEntity> findApprovedLeaveDraft(@Param("memberId") Long memberId,
                                                 @Param("draftId") Long draftId);
}
