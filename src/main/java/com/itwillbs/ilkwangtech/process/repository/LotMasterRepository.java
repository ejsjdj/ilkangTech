package com.itwillbs.ilkwangtech.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.process.entity.LotMaster;

public interface LotMasterRepository extends JpaRepository<LotMaster, String> {

	List<LotMaster> findByLotType(String lotType);
}
