package com.itwillbs.ilkwangtech.inventorymg.repository;

import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    // 임박 재고 카운트 (유통기한이 30일 이내로 남은 재고)
    @Query("SELECT COUNT(i) FROM InventoryEntity i WHERE i.expirationDate <= :imminentDate AND i.currentQuantity > 0")
    long countImminentStock(@Param("imminentDate") LocalDate imminentDate);
    
    // 창고(Zone/Rack)별 총 재고량 계산
    @Query("SELECT COALESCE(SUM(i.currentQuantity), 0) FROM InventoryEntity i WHERE i.zone = :zone AND i.rack = :rack")
    Long sumQuantityByZoneAndRack(@Param("zone") String zone, @Param("rack") String rack);
    
    // 특정 Zone과 Rack에 있는 재고 목록 조회 (수량이 0보다 큰 것만)
    @Query("SELECT i FROM InventoryEntity i JOIN FETCH i.item WHERE i.zone = :zone AND i.rack = :rack AND i.currentQuantity > 0")
    List<InventoryEntity> findByZoneAndRack(@Param("zone") String zone, @Param("rack") String rack);
    
    // 특정 품목의 창고 재고 총합 구하기
    @Query("SELECT COALESCE(SUM(i.currentQuantity), 0) FROM InventoryEntity i WHERE i.item.itemId = :itemId")
    Long sumCurrentQuantityByItemId(@Param("itemId") Long itemId);
    
    // 전체 재고량 그룹화 조회
    @Query("SELECT i.item.itemId, COALESCE(SUM(i.currentQuantity), 0) FROM InventoryEntity i GROUP BY i.item.itemId")
    List<Object[]> sumCurrentQuantityGrouped();

	List<InventoryEntity> findByItemItemCodeOrderByExpirationDateAsc(String itemCode);
	
	// 특정 프리픽스(품목코드-날짜)로 시작하는 LOT 번호 개수 조회 (순번 생성용)
    @Query("SELECT COUNT(i) FROM InventoryEntity i WHERE i.lotNumber LIKE :prefix%")
    long countByLotNumberStartingWith(@Param("prefix") String prefix);


    // 재고들의 현재 수량
    @Query("SELECT COALESCE(SUM(i.currentQuantity), 0) FROM InventoryEntity i WHERE i.item.itemCode = :itemCode")
    Long sumQuantityByItemCode(@Param("itemCode") String itemCode);
     
    // 현재 재고 확인
    @Query("""
        select coalesce(sum(i.currentQuantity),0)
        from InventoryEntity i
        where i.item.itemId = :itemId
        """)
    Long getTotalQuantityByItemId(@Param("itemId") Long itemId);

	List<InventoryEntity> findByItemItemIdOrderByExpirationDateAsc(Long itemId);

}