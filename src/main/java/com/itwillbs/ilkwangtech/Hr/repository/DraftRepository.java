package com.itwillbs.ilkwangtech.Hr.repository;

import com.itwillbs.ilkwangtech.Hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftRepository extends JpaRepository<DraftEntity, Long> {

    // 내가 등록한 결재 + 내가 결재자인 문서 등록
    @Query("""
    SELECT DISTINCT new com.itwillbs.ilkwangtech.Hr.dto.DraftDTO(
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

}
