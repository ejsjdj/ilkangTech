package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.production.dto.ProcessRegisterDTO;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRouteRepository extends JpaRepository<ProcessRouteEntity, Long> {

    // 1. 라우트 전체 조회
    @Query("""
            SELECT p
            FROM ProcessRouteEntity p
            WHERE p.id IN (
            SELECT MIN(p2.id)
            FROM ProcessRouteEntity p2
            GROUP BY p2.routeCode
            )
            """)
    Page<ProcessRouteEntity> findDistinctRouteIdBy(Pageable pageable);

    // 2. 라우트 상세 조회
    List<ProcessRouteEntity> findByRouteCodeOrderBySequenceAsc(String routeCode);

    // 3. 생산계획으로 작업지시 등록 용
    @Query("""
    SELECT new com.itwillbs.ilkwangtech.production.dto.ProcessRegisterDTO(
        pr.sequence,
        pr.operation.id,
        pr.operation.operationCode,
        pr.operation.name,
        pr.outItem.itemId,
        pr.outItem.itemName
    )
    FROM ProcessRouteEntity pr
    WHERE pr.item.id = :itemId
    ORDER BY pr.sequence ASC
    """)
    List<ProcessRegisterDTO> findProcessRegisterList(@Param("itemId") Long itemId);

    // 4. 라우트 순번 업데이트
//    @Modifying
//    @Query("""
//            UPDATE ProcessRouteEntity p
//            SET p.sequence = :sequence
//            WHERE p.id = :id
//            """)
//    void updateProcessRouteSeq(@Param("id") Long id,
//                               @Param("sequence") Long sequence);
//
//
//    // 5. 라우트 메모 업데이트
//    @Modifying
//    @Query("""
//            UPDATE ProcessRouteEntity p
//            SET p.note = :note
//            WHERE p.id = :id
//            """)
//    void updateProcessRouteMemo(@Param("id") Long id,
//                                @Param("memo") String note);

}
