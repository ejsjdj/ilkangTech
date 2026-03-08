package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneAllDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionPlaneDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionStatisticsDTO;
import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("""
            SELECT new com.itwillbs.ilkwangtech.production.dto.ProductionPlaneAllDTO(
            p.id,
            p.planeCode
            )
            FROM ProductionPlaneEntity p
            ORDER BY p.planeDate DESC
            """)
    List<ProductionPlaneAllDTO> findAllForSelect();


    // 금일 생산 계획
    @Query("""
            SELECT COUNT(p)
            FROM ProductionPlaneEntity p
            WHERE p.planeDate BETWEEN :startDate AND :endDate
            """)
    Long findTodayPlans(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 금일 완료 계획
    @Query("""
        SELECT COUNT(p)
        FROM ProductionPlaneEntity p
        WHERE p.status = 'COMPLETE'
        AND p.planeDate >= :start
        AND p.planeDate < :end
        """)
    Long findCompletePlans(@Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end);

    // 진행중인 생산계획
    @Query(""" 
            SELECT COUNT(p)
            FROM ProductionPlaneEntity p 
            WHERE p.status = 'PROGRESS'
            """)
    Long findProgressPlans();

    // 지연 생산계획
    @Query(""" 
            SELECT COUNT(p) 
            FROM ProductionPlaneEntity p 
            WHERE p.status = 'READY'
            """)
    Long findWaitingPlans();

    // 일별 생산량
    @Query("""
    SELECT 
    FUNCTION('TO_CHAR', p.planeDate, 'YYYY-MM-DD'),
    SUM(p.totalQty)
    FROM ProductionPlaneEntity p
    WHERE p.planeDate >= :startDate
    AND p.planeDate < :endDate
    AND p.status = 'COMPLETE'
    GROUP BY FUNCTION('TO_CHAR', p.planeDate, 'YYYY-MM-DD')
    ORDER BY FUNCTION('TO_CHAR', p.planeDate, 'YYYY-MM-DD')
    """)
    List<Object[]> getDailyProduction(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 주별 생산량
    @Query("""
        SELECT 
        FUNCTION('TO_CHAR', p.planeDate, 'IYYY-IW'),
        SUM(p.totalQty)
        FROM ProductionPlaneEntity p
        WHERE p.planeDate >= :startDate
        AND p.planeDate < :endDate
        AND p.status = 'COMPLETE'
        GROUP BY FUNCTION('TO_CHAR', p.planeDate, 'IYYY-IW')
        ORDER BY FUNCTION('TO_CHAR', p.planeDate, 'IYYY-IW')
        """)
    List<Object[]> getWeeklyProduction(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    // 월별 생산량
    @Query("""
        SELECT 
        FUNCTION('TO_CHAR', p.planeDate, 'YYYY-MM'),
        SUM(p.totalQty)
        FROM ProductionPlaneEntity p
        WHERE p.planeDate >= :startDate
        AND p.planeDate < :endDate
        AND p.status = 'COMPLETE'
        GROUP BY FUNCTION('TO_CHAR', p.planeDate, 'YYYY-MM')
        ORDER BY FUNCTION('TO_CHAR', p.planeDate, 'YYYY-MM')
        """)
    List<Object[]> getMonthlyProduction(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 품목별 생산수량
    @Query("""
       SELECT i.itemName,
              COALESCE(SUM(p.totalQty), 0)
       FROM ItemEntity i
       LEFT JOIN ProductionPlaneEntity p
       ON p.item = i
       AND p.status = 'COMPLETE'
       WHERE i.itemType = 3
       GROUP BY i.itemName
       ORDER BY i.itemName
       """)
    List<Object[]> findByItemQty();


}