package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.entity.BomEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BomRepository extends JpaRepository<BomEntity, Long> {

	// 💡 전체 생산계획 종속 소요량 그룹화 조회 (p.item은 객체이므로 .itemId 사용!)
    @Query("SELECT b.childItem.itemId, COALESCE(SUM(p.totalQty * b.requireQty), 0) " +
           "FROM ProductionPlaneEntity p, BomEntity b " +
           "WHERE p.item.itemId = b.parentItem.itemId AND p.status != 'CAN' " +
           "GROUP BY b.childItem.itemId")
    List<Object[]> sumDependentPlanQtyGrouped();

    // 💡 전체 작업지시 종속 소요량 그룹화 조회 (i.item은 숫자이므로 그냥 사용!)
    @Query("SELECT b.childItem.itemId, COALESCE(SUM(i.instructQty * b.requireQty), 0) " +
           "FROM ProductionInstructEntity i, BomEntity b " +
           "WHERE i.item.itemId = b.parentItem.itemId AND i.status NOT IN ('COM', 'CAN') " +
           "GROUP BY b.childItem.itemId")
    List<Object[]> sumDependentInstructQtyGrouped();
    
    
}