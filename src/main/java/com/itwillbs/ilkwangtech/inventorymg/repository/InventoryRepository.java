package com.itwillbs.ilkwangtech.inventorymg.repository;

import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    
    // 창고(Zone/Rack)별 총 재고량 계산
	@Query("SELECT COALESCE(SUM(i.currentQuantity), 0) FROM InventoryEntity i WHERE i.zone = :zone AND i.rack = :rack")
    Long sumQuantityByZoneAndRack(@Param("zone") String zone, @Param("rack") String rack);
	
	// 특정 Zone과 Rack에 있는 재고 목록 조회 (수량이 0보다 큰 것만)
    @Query("SELECT i FROM InventoryEntity i JOIN FETCH i.item WHERE i.zone = :zone AND i.rack = :rack AND i.currentQuantity > 0")
    List<InventoryEntity> findByZoneAndRack(@Param("zone") String zone, @Param("rack") String rack);
    
}