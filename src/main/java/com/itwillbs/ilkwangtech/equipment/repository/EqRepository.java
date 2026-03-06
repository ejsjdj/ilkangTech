package com.itwillbs.ilkwangtech.equipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwillbs.ilkwangtech.equipment.entity.Equipment;

public interface EqRepository extends JpaRepository<Equipment, Long> {

	// 설비 현황 조회
	@Query(value = "SELECT equipment_seq.NEXTVAL FROM dual", nativeQuery = true)
    Long nextId();
	
	
}
