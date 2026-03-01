package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequestEntity, Long> {
	
	@Query("SELECT COALESCE(SUM(pr.quantity), 0) FROM PurchaseRequestEntity pr WHERE pr.item.itemId = :itemId")
	Long sumQuantityByItemId(@Param("itemId") Long itemId);
	
	// 전체 발주대기량 그룹화 조회
    @Query("SELECT pr.item.itemId, COALESCE(SUM(pr.quantity), 0) FROM PurchaseRequestEntity pr GROUP BY pr.item.itemId")
    List<Object[]> sumQuantityGrouped();

}