package com.itwillbs.ilkwangtech.Hr.repository;

import com.itwillbs.ilkwangtech.Hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.Hr.entity.DraftEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftRepository extends JpaRepository<DraftEntity, Long> {

    @Query("""
            SELECT new com.itwillbs.ilkwangtech.Hr.dto.DraftDTO(
                d.draftId,
                d.draftTitle,
                d.draftStartDate,
                d.draftEndDate,
                d.draftStatus
            )
            FROM DraftEntity d
            WHERE d.member.id = :userId
            """)
    List<DraftDTO> findByMember_Id(Long userId);

    @Query("""
            SELECT new com.itwillbs.ilkwangtech.Hr.dto.DraftDTO(
                d.draftId,
                    d.draftTitle,
                        d.draftStartDate,
                            d.draftEndDate,
                                d.draftStatus
                )
            FROM DraftApproveStatusEntity r
            JOIN r.draftEntity d
            WHERE r.member.id = :userId
            """)
    List<DraftDTO> findApproveDrafts(@Param("userId") Long userId);

}
