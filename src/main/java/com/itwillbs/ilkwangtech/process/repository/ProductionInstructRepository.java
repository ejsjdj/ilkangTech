package com.itwillbs.ilkwangtech.process.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.process.entity.ProductionInstruct;

public interface ProductionInstructRepository extends JpaRepository<ProductionInstruct, Long> {

	@Query(value = "SELECT p.instruct_code AS instructCode, i.item_name AS itemName, " +
            "p.instruct_qty AS instructQty, p.defective AS defective, " +
            "p.status AS status, " +
            "o.name AS operationName, " +
            "p.start_date AS startDate, p.end_date AS endDate " +
            "FROM production_instruct p " +
            "LEFT JOIN item i ON p.item_id = i.item_id " +
            "LEFT JOIN production_worker w ON p.id = w.instruct_id AND w.status = 'PROGRESS' " +
            "LEFT JOIN operation_info o ON w.process_id = o.id " + 
            "ORDER BY p.start_date DESC NULLS LAST", nativeQuery = true)
	List<ProcessStatusMapping> findAllProcessStatus();
	
	interface ProcessStatusMapping {
	 String getInstructCode();
	 String getItemName();
	 Integer getInstructQty();
	 Integer getDefective();
	 String getStatus();
	 String getOperationName(); 
	 LocalDateTime getStartDate();
	 LocalDateTime getEndDate();
	}
	
	@Query(value = "SELECT " +
            "    o.name AS processName, " + 
            "    w.status AS status, " +
            "    p.start_date AS startDate, " + 
            "    p.end_date AS endDate, " +
            "    p.defective AS defectiveQty " + 
            "FROM production_worker w " +
            // 1. 작업자 실적(w)과 지시(p) 연결
            "JOIN production_instruct p ON w.instruct_id = p.id " +
            // 2. 작업자 실적(w.process_id)과 공정 정보(o.id) 연결 
            "JOIN operation_info o ON w.process_id = o.id " +
            "WHERE p.instruct_code = :instructCode " +
            "ORDER BY w.id ASC", nativeQuery = true)
	List<DetailMapping> findStepsByInstructCode(@Param("instructCode") String instructCode);
	
	interface DetailMapping {
	 String getProcessName();
	 String getStatus();
	 LocalDateTime getStartDate();
	 LocalDateTime getEndDate();
	 Integer getDefectiveQty();
	}

	@Query(value = "SELECT p.instruct_code AS instructCode, i.item_name AS itemName, " +
            "p.instruct_qty AS instructQty, " +
            "p.defective AS defectiveQty " +
            "FROM production_instruct p " +
            "JOIN item i ON p.item_id = i.item_id " +
            "WHERE p.instruct_code = :instructCode", nativeQuery = true)
	HeaderMapping findHeaderByCode(@Param("instructCode") String instructCode);

    interface HeaderMapping {
        String getInstructCode();
        String getItemName();
        Integer getInstructQty();
		Integer getDefectiveQty();
    }
}
