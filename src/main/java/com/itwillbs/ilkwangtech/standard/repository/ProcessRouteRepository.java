package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.dto.ProcessRouteDetailDTO;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRouteRepository extends JpaRepository<ProcessRouteEntity, Long> {

    // 1. 라우트 전체 조회
    @Query("""
                SELECT p FROM ProcessRouteEntity p
                WHERE p.id IN (
                SELECT MAX(p2.id)
                FROM ProcessRouteEntity p2
                WHERE (:routeName IS NULL OR p2.routeName LIKE %:routeName%)
                AND (:itemId IS NULL OR p2.item.id = :itemId)
                GROUP BY p2.routeCode
                )
                """)
    Page<ProcessRouteEntity> findDistinctRouteIdBy(
            @Param("itemId") Long itemId,
            @Param("routeName") String routeName,
            Pageable pageable
    );

    //List<ProcessRouteEntity> findByRouteCodeOrderBySequenceAsc(String routeCode);
}
