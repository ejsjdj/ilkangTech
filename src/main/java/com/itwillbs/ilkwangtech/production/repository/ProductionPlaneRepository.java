package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionPlaneRepository extends JpaRepository<ProductionPlaneEntity, Long> {
}
