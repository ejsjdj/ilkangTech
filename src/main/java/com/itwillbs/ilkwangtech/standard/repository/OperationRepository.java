package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository <OperationEntity, Long> {
}
