package com.itwillbs.ilkwangtech.Hr.repository;

import com.itwillbs.ilkwangtech.Hr.entity.DraftApproveStatusEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftApproveStatusRepository extends JpaRepository<DraftApproveStatusEntity, String> {

    List<DraftApproveStatusEntity> findByDraftEntity_DraftId(Long draftId);

    @Query("SELECT r FROM DraftApproveStatusEntity r " +
            "JOIN FETCH r.draftEntity " +
            "WHERE r.member.id = :userId")
    List<DraftApproveStatusEntity> findByMemberIdWithDraft(@Param("userId") Long userId);


    // TODO :: 승인 반려 구현
    /*@Query("""
    update DraftApprovalStatus s
    set s.status = :status
    where s.member.id = :memberId
    and s.draft.id = :draftId
    """)int updateStatus(
            Long memberId,
            Long draftId,
            String status
    );*/
}
