package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
    
    Optional<WareHouse> findByWarehouseId(Long id);
    
    Optional<WareHouse> findByWarehouseName(String warehouseName);
    
    @Query("SELECT w FROM WareHouse w WHERE w.warehouseName LIKE CONCAT('%', :warehouseName, '%')")
    List<WareHouse> findByWarehouseNameContaining(@Param("warehouseName") String warehouseName);
    
    @Query("SELECT w FROM WareHouse w JOIN w.itemStocks i GROUP BY w.warehouseId ORDER BY COUNT(i.itemStockId) DESC")
    List<WareHouse> findWarehousesWithMostItems();
}
