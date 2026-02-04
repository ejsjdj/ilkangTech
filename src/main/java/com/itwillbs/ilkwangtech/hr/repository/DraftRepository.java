package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.hr.dto.LeaveByDepartmentDTO;
import com.itwillbs.ilkwangtech.hr.entity.DraftEntity;

import com.itwillbs.ilkwangtech.hr.entity.LeaveEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                    ") " +
                    "FROM DraftEntity d " +
                    "JOIN d.member m " +
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



    // 내가 작성한 결재 문서 조회 (기안함)
    @Query(value = """
         SELECT new com.itwillbs.ilkwangtech.hr.dto.DraftDTO(
                        d.draftId,
                        d.draftTitle,
                        d.draftStartDate,  
                        d.draftEndDate,    
                        d.draftStatus      
                    )
        FROM DraftEntity d
        WHERE d.member.id = :userId
          AND d.draftStartDate BETWEEN :startDate AND :endDate
          AND d.draftType = :draftType
    """, countQuery = """
        SELECT COUNT(d.draftId) FROM DraftEntity d 
        WHERE d.member.id = :userId 
          AND d.draftStartDate BETWEEN :startDate AND :endDate
          AND d.draftType = :draftType
    """)
    Page<DraftDTO> findSentDrafts(@Param("userId") Long userId,
                                  @Param("draftType") String draftType,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  Pageable pageable);

    // 내가 결재자인 문서 조회 (결재함)
    @Query(
            value = """
    SELECT new com.itwillbs.ilkwangtech.hr.dto.DraftDTO(
        d.draftId,
        d.draftTitle,
        d.draftStartDate,
        d.draftEndDate,
        d.draftStatus
    )
    FROM DraftApproveStatusEntity r
    JOIN r.draftEntity d
    WHERE r.member.id = :userId
      AND d.draftStartDate BETWEEN :startDate AND :endDate
      AND d.draftType = :draftType
""",
            countQuery = """
    SELECT COUNT(DISTINCT d.draftId)
    FROM DraftApproveStatusEntity r
    JOIN r.draftEntity d
    WHERE r.member.id = :userId
      AND d.draftStartDate BETWEEN :startDate AND :endDate
      AND d.draftType = :draftType
"""
    )
    Page<DraftDTO> findReceivedDrafts(@Param("userId") Long userId,
                                      @Param("draftType") String draftType,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate,
                                      Pageable pageable);

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
