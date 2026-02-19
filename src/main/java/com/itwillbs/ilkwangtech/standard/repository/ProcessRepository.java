package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.dto.ProcessCodeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ProcessDetailDTO;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {

    // 1. 라우트 전체 조회
    @Query("SELECT p FROM ProcessEntity p " +
            "WHERE p.id IN (SELECT MAX(p2.id) FROM ProcessEntity p2 GROUP BY p2.routeId) " +
            "ORDER BY p.routeId ASC")
    Page<ProcessEntity> findDistinctRouteIdBy(Pageable pageable);

    // 2. 라우트 상세 조회
    List<ProcessDetailDTO> findByRouteId(String routeId);
}
