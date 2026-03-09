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

	@Query(value = "SELECT pi.instruct_code AS instructCode, " +
	            "       MAX(i.item_name) AS itemName, " +
	            "       MAX(pi.instruct_qty) AS instructQty, " +
	            "       MAX(NVL(pi.defective, 0)) AS defective, " +
	            "       MAX(pi.status) AS status, " +
	            "       MAX(oi.description) AS operationName, " +
	            // 날짜 데이터를 프론트엔드에서 파싱하기 좋게 ISO 포맷의 문자열로 강제 변환합니다.
	            "       TO_CHAR(MIN(pw.start_time), 'YYYY-MM-DD\"T\"HH24:MI:SS') AS startDate, " +
	            "       TO_CHAR(MAX(pw.end_time), 'YYYY-MM-DD\"T\"HH24:MI:SS') AS endDate " +
	            "FROM production_instruct pi " +
	            // 타입 충돌(ORA-01722) 방지를 위해 양쪽 모두 TO_CHAR로 감싸서 비교합니다.
	            "LEFT JOIN item i ON TO_CHAR(pi.item_id) = TO_CHAR(i.item_id) " +
	            "LEFT JOIN operation_info oi ON TO_CHAR(pi.operation_id) = TO_CHAR(oi.operation_id) " +
	            "LEFT JOIN production_worker pw ON pi.id = pw.instruct_id " +
	            // 코드를 기준으로 묶고 나머지는 MAX로 가져와서 쿼리를 가볍고 안전하게 만듭니다.
	            "GROUP BY pi.instruct_code " +
	            "ORDER BY pi.instruct_code DESC", nativeQuery = true)
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