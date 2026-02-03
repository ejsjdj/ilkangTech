package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemStockRepository extends JpaRepository<ItemStock, Long> {
    
    Optional<ItemStock> findByItemStockId(Long id);
    
    List<ItemStock> findByWarehouse_WarehouseId(Long warehouseId);
    
    @Query("SELECT i FROM ItemStock i WHERE i.productName LIKE CONCAT('%', :productName, '%')")
    List<ItemStock> findByProductNameContaining(@Param("productName") String productName);
    
    @Query("SELECT i FROM ItemStock i WHERE i.quantity > 0")
    List<ItemStock> findAvailableStock();
    
    @Query("SELECT i FROM ItemStock i WHERE i.productCode = :productCode")
    Optional<ItemStock> findByProductCode(@Param("productCode") String productCode);
}
