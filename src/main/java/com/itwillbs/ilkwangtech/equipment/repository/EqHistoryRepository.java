package com.itwillbs.ilkwangtech.equipment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.equipment.entity.EqHistory;

public interface EqHistoryRepository extends JpaRepository<EqHistory, Long> {
	
	List<EqHistory> findAllByOrderByStartTimeDesc();
}
