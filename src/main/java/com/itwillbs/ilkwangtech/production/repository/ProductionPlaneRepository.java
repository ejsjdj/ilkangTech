package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductionPlaneRepository extends JpaRepository<ProductionPlaneEntity, Long> {


    @Query("""
            SELECT p FROM ProductionPlaneEntity p
            WHERE :keyword IS NULL OR p.planeCode LIKE CONCAT('%', :keyword, '%')
            """)
    Page<ProductionPlaneEntity> findByKeyword(Pageable pageable,
                                           @Param("keyword") String keyword);

    @Query("""
        SELECT DISTINCT p
        FROM ProductionPlaneEntity p
        LEFT JOIN FETCH p.details d
        LEFT JOIN FETCH d.item
        LEFT JOIN FETCH p.member
        LEFT JOIN FETCH p.item
        WHERE p.id = :id
        """)
    Optional<ProductionPlaneEntity> findDetailById(@Param("id") Long id);


    // 1. 생산 계획 상태 업데이트
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductionPlaneEntity p SET p.status = 'CANCEL' WHERE p.id = :planeId")
    int updatePlaneStatus(@Param("planeId") Long planeId);

    // 2. 해당 계획에 속한 모든 생산 지시 상태 업데이트
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductionInstructEntity i SET i.status = 'CANCEL' WHERE i.productionId.id = :planeId")
    int updateInstructStatusByPlaneId(@Param("planeId") Long planeId);

}