package com.itwillbs.ilkwangtech.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.process.entity.QualityCheck;

public interface QualityCheckRepository extends JpaRepository<QualityCheck, String> {

	List<QualityCheck> findByLotMaster_LotId(String lotId);
}
