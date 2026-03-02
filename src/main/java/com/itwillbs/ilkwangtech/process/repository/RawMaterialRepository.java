package com.itwillbs.ilkwangtech.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.process.entity.RawMaterial;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, String> {
	
	List<RawMaterial> findByLotMaster_LotId(String lotId);
}
