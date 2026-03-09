package com.itwillbs.ilkwangtech.process.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.process.entity.ProductionInstruct;

import jakarta.transaction.Transactional;

public interface ProductionInstructRepository extends JpaRepository<ProductionInstruct, Long> {

	@Query(value = "SELECT p.instruct_code AS instructCode, i.item_name AS itemName, " +
            "p.instruct_qty AS instructQty, p.defective AS defective, " +
            "p.status AS status, o.name AS operationName, " +
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
            "    w.start_time AS startDate, " +      // <-- 수정: w.start_time
            "    w.end_time AS endDate, " +          // <-- 수정: w.end_time
            "    w.defective_qty AS defectiveQty " + 
            "FROM production_worker w " +
            "JOIN production_instruct p ON w.instruct_id = p.id " +
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

    // ✅ 수정됨: 중복 에러(400) 방지를 위해 List로 반환
	@Query(value = "SELECT p.instruct_code AS instructCode, i.item_name AS itemName, " +
            "p.instruct_qty AS instructQty, " +
            "p.defective AS defectiveQty " +
            "FROM production_instruct p " +
            "JOIN item i ON p.item_id = i.item_id " +
            "WHERE p.instruct_code = :instructCode", nativeQuery = true)
	List<HeaderMapping> findHeaderByCode(@Param("instructCode") String instructCode); // 반환 타입 List로 변경

    interface HeaderMapping {
        String getInstructCode();
        String getItemName();
        Integer getInstructQty();
		Integer getDefectiveQty();
    }
    
    @Query(value = "SELECT p.instruct_code AS instructCode, i.item_name AS itemName, " +
            "o.name AS processName, w.defective_qty AS defectiveQty, " +
            "w.end_time AS endDate " +
            "FROM production_worker w " +
            "JOIN production_instruct p ON w.instruct_id = p.id " +
            "JOIN item i ON p.item_id = i.item_id " +
            "LEFT JOIN operation_info o ON w.process_id = o.id " +
            "WHERE w.defective_qty > 0 " +
            "ORDER BY w.end_time DESC", nativeQuery = true)
    List<DefectiveMapping> findDefectiveProcesses();
    
    interface DefectiveMapping {
        String getInstructCode();
        String getItemName();
        String getProcessName();
        Integer getDefectiveQty();
        LocalDateTime getEndDate();
    }
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE production_worker SET start_time = SYSDATE, status = 'PROGRESS' WHERE id = :workerId", nativeQuery = true)
    void updateStartTime(@Param("workerId") Long workerId);

    // 작업 종료 시간 업데이트
    @Modifying
    @Transactional
    @Query(value = "UPDATE production_worker SET end_time = SYSDATE, status = 'COMPLETE' WHERE id = :workerId", nativeQuery = true)
    void updateEndTime(@Param("workerId") Long workerId);
    
    
    
    
}