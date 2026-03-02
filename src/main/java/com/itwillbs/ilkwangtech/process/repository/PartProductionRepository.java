package com.itwillbs.ilkwangtech.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.process.entity.PartProduction;

public interface PartProductionRepository extends JpaRepository<PartProduction, String> {

	List<PartProduction> findByLotMaster_LotId(String lotId);
}
