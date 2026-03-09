package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.entity.ProductionStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionStorageRepository extends JpaRepository<ProductionStorageEntity, Long> {
}
