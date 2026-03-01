package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {

    List<PurchaseOrderEntity> findByHeaderId(Long headerId);
    
    // 입고 예정인 발주 건수 조회 (출하완료 또는 검수완료 상태)
    @Query("SELECT COUNT(h) FROM PurchaseOrderHeaderEntity h WHERE h.status = 'COMPLETE'")
    long countInboundScheduled();
    
    // 상태가 'COMPLETE'인 발주 목록과 상세 라인을 한 번에 조회
    @Query("SELECT DISTINCT h FROM PurchaseOrderHeaderEntity h LEFT JOIN FETCH h.lines WHERE h.status = 'COMPLETE'")
    List<PurchaseOrderHeaderEntity> findCompleteOrders();
    
    // 특정 품목의 입고 예정(CONFIRMED, COMPLETE 상태) 수량 총합 구하기
    @Query("SELECT COALESCE(SUM(po.quantity), 0) FROM PurchaseOrderEntity po " +
           "WHERE po.item.itemId = :itemId AND po.header.status IN ('CONFIRMED', 'COMPLETE')")
    Long sumIncomingQuantityByItemId(@Param("itemId") Long itemId);

}
