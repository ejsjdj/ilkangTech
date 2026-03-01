package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequestEntity, Long> { // 구매 요청 라인용
}