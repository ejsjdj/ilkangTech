package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.entity.BomEntity;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BomRepository extends JpaRepository<BomEntity, Long> {

}
