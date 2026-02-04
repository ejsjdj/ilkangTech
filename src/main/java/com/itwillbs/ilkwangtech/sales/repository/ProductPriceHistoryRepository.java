package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceHistoryRepository extends JpaRepository<ProductPriceHistory, Long> {
    
    // 특정 제품의 현재 유효한 단가 조회
    @Query("SELECT p FROM ProductPriceHistory p WHERE p.itemStock.itemStockId = :itemStockId " +
           "AND (p.customer.customerId = :customerId OR p.customer IS NULL) " +
           "AND p.effectiveDate <= :currentDate AND (p.endDate IS NULL OR p.endDate > :currentDate) " +
           "ORDER BY p.customer.customerId DESC, p.effectiveDate DESC")
    Optional<ProductPriceHistory> findCurrentPrice(@Param("itemStockId") Long itemStockId, 
                                                   @Param("customerId") Long customerId,
                                                   @Param("currentDate") LocalDateTime currentDate);
    
    // 특정 제품의 모든 단가 이력 조회
    List<ProductPriceHistory> findByItemStock_ItemStockIdOrderByEffectiveDateDesc(Long itemStockId);
    
    // 특정 고객사의 제품 단가 이력 조회
    List<ProductPriceHistory> findByCustomer_CustomerIdAndItemStock_ItemStockIdOrderByEffectiveDateDesc(
            Long customerId, Long itemStockId);
    
    // 특정 기간 동안의 단가 변경 이력 조회
    @Query("SELECT p FROM ProductPriceHistory p WHERE p.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY p.createdAt DESC")
    List<ProductPriceHistory> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    // 모든 제품의 현재 유효한 단가 목록 조회
    @Query("SELECT p FROM ProductPriceHistory p WHERE p.effectiveDate <= :currentDate " +
           "AND (p.endDate IS NULL OR p.endDate > :currentDate) " +
           "ORDER BY p.itemStock.productName, p.customer.customerName")
    List<ProductPriceHistory> findAllCurrentPrices(@Param("currentDate") LocalDateTime currentDate);
    
    // 특정 제품의 이전 단가 종료 처리
    @Query("UPDATE ProductPriceHistory p SET p.endDate = :endDate WHERE p.itemStock.itemStockId = :itemStockId " +
           "AND (p.customer.customerId = :customerId OR p.customer IS NULL) " +
           "AND p.endDate IS NULL")
    void endPreviousPrices(@Param("itemStockId") Long itemStockId, 
                          @Param("customerId") Long customerId,
                          @Param("endDate") LocalDateTime endDate);
}
