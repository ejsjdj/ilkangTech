package com.itwillbs.ilkwangtech.Hr.repository;

import com.itwillbs.ilkwangtech.Hr.entity.DraftApproveStatusEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DraftApproveStatusRepository extends JpaRepository<DraftApproveStatusEntity, String> {

    List<DraftApproveStatusEntity> findByDraftEntity_DraftId(Long draftId);

    // 이미 승인 또는 반려된 문서 체크
    @Query("SELECT r FROM DraftApproveStatusEntity r " +
            "JOIN FETCH r.draftEntity " +
            "WHERE r.member.id = :userId AND r.draftEntity.draftId = :draftId")
    Optional<DraftApproveStatusEntity> findByMemberIdWithDraft(
            @Param("userId") Long userId,
            @Param("draftId") Long draftId);

    // 결재 상태 업데이트
    @Modifying(clearAutomatically = true)
    @Query("""
    update DraftApproveStatusEntity s
    set s.status = :decision
    where s.member.id = :memberId
    and s.draftEntity.draftId = :draftId
    """)int updateStatus(
            Long memberId,
            Long draftId,
            String decision
    );
}
