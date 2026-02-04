package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.WarehouseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WarehouseTransactionRepository extends JpaRepository<WarehouseTransaction, Long> {
    
    List<WarehouseTransaction> findByWarehouse_WarehouseId(Long warehouseId);
    
    List<WarehouseTransaction> findByItemStock_ItemStockId(Long itemStockId);
    
    List<WarehouseTransaction> findByTransactionType(String transactionType);
    
    @Query("SELECT t FROM WarehouseTransaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<WarehouseTransaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM WarehouseTransaction t WHERE t.warehouse.warehouseId = :warehouseId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<WarehouseTransaction> findByWarehouseAndDateBetween(@Param("warehouseId") Long warehouseId,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM WarehouseTransaction t WHERE t.itemStock.productName LIKE CONCAT('%', :productName, '%')")
    List<WarehouseTransaction> findByProductNameContaining(@Param("productName") String productName);
    
    @Query("SELECT t FROM WarehouseTransaction t WHERE t.referenceNumber = :referenceNumber")
    List<WarehouseTransaction> findByReferenceNumber(@Param("referenceNumber") String referenceNumber);
    
    @Query("SELECT t FROM WarehouseTransaction t ORDER BY t.transactionDate DESC")
    List<WarehouseTransaction> findAllOrderByDateDesc();
}
