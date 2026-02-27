package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestHeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestHeaderRepository extends JpaRepository<PurchaseRequestHeaderEntity, Long> {
}