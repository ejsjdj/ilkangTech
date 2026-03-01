package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.entity.BomEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BomRepository extends JpaRepository<BomEntity, Long> {

    // 생산계획에 의한 원자재 종속 소요량 계산 (계획수량 * BOM필요수량)
    @Query("SELECT COALESCE(SUM(p.totalQty * b.requireQty), 0) " +
           "FROM ProductionPlaneEntity p, BomEntity b " +
           "WHERE p.item = b.afterItem.itemId " +
           "AND b.beforeItem.itemId = :itemId " +
           "AND p.status != 'CAN'")
    Long sumDependentPlanQty(@Param("itemId") Long itemId);

    // 작업지시에 의한 원자재 종속 소요량 계산 (예약재고)
    @Query("SELECT COALESCE(SUM(i.instructQty * b.requireQty), 0) " +
           "FROM ProductionInstructEntity i, BomEntity b " +
           "WHERE i.item = b.afterItem.itemId " +
           "AND b.beforeItem.itemId = :itemId " +
           "AND i.status NOT IN ('COM', 'CAN')")
    Long sumDependentInstructQty(@Param("itemId") Long itemId);
}