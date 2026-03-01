package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {

    List<PurchaseOrderEntity> findByHeaderId(Long headerId);

    @Query("""
        SELECT h
        FROM PurchaseOrderHeaderEntity h
        LEFT JOIN FETCH h.lines
        WHERE h.id = :id
    """)
    PurchaseOrderHeaderEntity findByPurchaseIdDetail(@Param("purchaseOrderId") Long purchaseOrderId);
    
    @Query("SELECT COUNT(h) FROM PurchaseOrderHeaderEntity h WHERE h.status = 'COMPLETE'")
    long countInboundScheduled();
    
    // 상태가 'COMPLETE'인 발주 목록과 상세 라인을 한 번에 조회
    @Query("SELECT DISTINCT h FROM PurchaseOrderHeaderEntity h LEFT JOIN FETCH h.lines WHERE h.status = 'COMPLETE'")
    List<PurchaseOrderHeaderEntity> findCompleteOrders();

}
