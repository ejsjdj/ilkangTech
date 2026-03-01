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

import java.util.List;
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
            SELECT p FROM ProductionPlaneEntity p
            LEFT JOIN FETCH p.details
            WHERE p.id = :id
            """)
    Optional<ProductionPlaneEntity> findDetailById(@Param("id") Long id);


    // 생산 계획 상태 업데이트
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductionPlaneEntity p SET p.status = 'CAN' WHERE p.id = :planeId")
    int updatePlaneStatus(@Param("planeId") Long planeId);

    // 해당 계획에 속한 모든 생산 지시 상태 업데이트
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductionInstructEntity i SET i.status = 'CAN' WHERE i.productionId.id = :planeId")
    int updateInstructStatusByPlaneId(@Param("planeId") Long planeId);
    
    // 생산계획 총 수량 조회    
    @Query("SELECT COALESCE(SUM(p.totalQty), 0) FROM ProductionPlaneEntity p WHERE p.item.itemId = :itemId AND p.status != 'CAN'")
    Long sumProductionPlanQtyByItemId(@Param("itemId") Long itemId);
    
    // 전체 생산계획량 그룹화 조회 (item이 단순 숫자형)
    @Query("SELECT p.item.itemId, COALESCE(SUM(p.totalQty), 0) FROM ProductionPlaneEntity p WHERE p.status != 'CAN' GROUP BY p.item.itemId")
    List<Object[]> sumProductionPlanQtyGrouped();

}