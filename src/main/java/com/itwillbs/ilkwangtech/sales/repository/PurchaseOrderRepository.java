package com.itwillbs.ilkwangtech.sales.repository;


import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderDetailDTO;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderEntity;
import com.itwillbs.ilkwangtech.sales.entity.PurchaseOrderHeaderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {

    // 1. 발주 리스트 조회
    @Query("""
        SELECT p FROM PurchaseOrderHeaderEntity p
        WHERE (:searchType IS NULL OR p.status = :searchType)
        AND (:keyword IS NULL OR p.purchaseOrderCode LIKE CONCAT('%', :keyword, '%'))
        AND (
            (:startDate IS NULL OR p.orderDate >= :startDate)
            AND (:endDate IS NULL OR p.orderDate <= :endDate)
            )
        """)
    Page<PurchaseOrderHeaderEntity> findByPurchaseOrder(
            Pageable pageable,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword);

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
