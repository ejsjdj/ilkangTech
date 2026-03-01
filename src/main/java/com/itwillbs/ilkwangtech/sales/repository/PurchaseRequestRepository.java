package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequestEntity, Long> {
	
	@Query("SELECT COALESCE(SUM(pr.quantity), 0) FROM PurchaseRequestEntity pr WHERE pr.itemId = :itemId")
    Long sumQuantityByItemId(@Param("itemId") Long itemId);
	
}