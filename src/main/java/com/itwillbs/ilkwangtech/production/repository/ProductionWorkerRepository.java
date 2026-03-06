package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionWorkerRepository extends JpaRepository<ProductionWorkerEntity, Long> {
}
