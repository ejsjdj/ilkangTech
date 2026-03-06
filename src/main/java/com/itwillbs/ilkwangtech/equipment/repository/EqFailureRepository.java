package com.itwillbs.ilkwangtech.equipment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.equipment.entity.EqFailure;

public interface EqFailureRepository extends JpaRepository<EqFailure, Long> {

	// 고장 내역 조회
		List<EqFailure> findAllByOrderByOccurredAtDesc();
}
