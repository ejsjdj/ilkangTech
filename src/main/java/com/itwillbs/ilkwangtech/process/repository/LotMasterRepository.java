package com.itwillbs.ilkwangtech.process.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.process.entity.LotMaster;

public interface LotMasterRepository extends JpaRepository<LotMaster, String> {

	List<LotMaster> findByLotType(String lotType);
	
	@Query(value = "SELECT l.lot_id AS lotId, i.item_name AS itemName, l.status AS status, " +
            "l.created_date AS createdDate, l.lot_type AS lotType " + // lotType 추가
            "FROM lot_master l " +
            "LEFT JOIN item i ON l.product_id = i.item_id", nativeQuery = true)
	List<LotSummaryMapping> findAllWithItemName();
	
	interface LotSummaryMapping {
	 String getLotId();
	 String getItemName();
	 String getStatus();
	 String getLotType();
	 java.time.LocalDateTime getCreatedDate();
	}

	interface LotDetailMapping {
	    String getLotId();
	    String getItemCode();   // 추가됨
	    String getItemName();
	    String getInstructCode();
	    Integer getInstructQty();
	    LocalDateTime getStartDate();
	    LocalDateTime getEndDate();
	    Integer getDefective();
	}
	
	@Query(value = "SELECT lm.lot_id AS lotId, i.item_name AS itemName, " +
	            "lm.quantity AS quantity, lm.status AS status " +
	            "FROM ( " +
	            "    SELECT lot_id, product_id, quantity, status, lot_type " +
	            "    FROM LOT_MASTER " +
	            "    START WITH lot_id = :rawLotId " + // 입력받은 LOT부터 시작
	            "    CONNECT BY PRIOR lot_id = parent_lot_id " + // 자식 방향으로 전개
	            ") lm " +
	            "JOIN item i ON lm.product_id = i.item_id " +
	            "WHERE lm.lot_type = 'F'", nativeQuery = true) // 최종 완제품만 필터링
	List<FinishedUsageMapping> findFinishedLotsByRawLotId(@Param("rawLotId") String rawLotId);
	
	@Query(value = "SELECT lm.lot_id AS lotId, i.item_code AS itemCode, i.item_name AS itemName, " +
	            "pi.instruct_code AS instructCode, pi.instruct_qty AS instructQty, " +
	            "pi.start_date AS startDate, pi.end_date AS endDate, pi.defective AS defective " +
	            "FROM lot_master lm " +
	            "JOIN item i ON lm.product_id = i.item_id " +
	            "JOIN production_worker pw ON lm.lot_id = pw.lot_id " + // PW를 거점으로 연결
	            "JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	            "WHERE lm.lot_id = :lotId", nativeQuery = true)
	LotDetailMapping findSemiLotDetail(@Param("lotId") String lotId);
	
	@Query(value = "SELECT iv.lot_number AS lotId, it.item_name AS itemName, " +
            "'DONE' AS status, iv.expiration_date AS createdDate, 'M' AS lotType " +
            "FROM inventory iv " +
            "JOIN item it ON iv.item_id = it.item_id", nativeQuery = true)
	List<LotMasterRepository.LotSummaryMapping> findAllInventoryAsLots();
	
	@Query(value = "SELECT lm.lot_id AS lotId, i.item_code AS itemCode, i.item_name AS itemName, " +
	            "pi.instruct_code AS instructCode, pi.instruct_qty AS instructQty, " +
	            "pi.start_date AS startDate, pi.end_date AS endDate, pi.defective AS defective " +
	            "FROM lot_master lm " +
	            "JOIN item i ON lm.product_id = i.item_id " +
	            "JOIN production_worker pw ON lm.lot_id = pw.lot_id " +
	            "JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	            "WHERE lm.lot_id = :lotId", nativeQuery = true)
	LotDetailMapping findFinishedLotDetail(@Param("lotId") String lotId);
	
	interface FinishedUsageMapping {
	 String getLotId();
	 String getItemName();
	 Integer getQuantity();
	 String getStatus();
	}
	
	@Query(value = "SELECT lotId, itemName, createdDate, lotType, status " +
	        "FROM ( " +
	        "    SELECT a.lot_id AS lotId, i.item_name AS itemName, " +
	        "           CAST(a.assembly_date AS TIMESTAMP) AS createdDate, 'F' AS lotType, 'DONE' AS status " + // CAST 추가
	        "    FROM ASSEMBLY a " +
	        "    JOIN LOT_MASTER lm ON a.lot_id = lm.lot_id " +
	        "    JOIN item i ON lm.product_id = i.item_id " +
	        "    UNION ALL " +
	        "    SELECT lot_id AS lotId, part_name AS itemName, " +
	        "           CAST(production_date AS TIMESTAMP) AS createdDate, 'S' AS lotType, 'DONE' AS status " + // CAST 추가
	        "    FROM PART_PRODUCTION " +
	        ") " +
	        "ORDER BY createdDate DESC", nativeQuery = true)
	List<LotSummaryMapping> findAllProdAndSemiLots();
	
	/* LotMasterRepository.java */

	@Query(value = "SELECT oi.description AS operationName, " +
	               "       oi.operation_id AS operationId, " +
	               "       oi.member_id AS memberId, " +
	               "       m.name AS memberName, " +
	               "       pi.operation_qty AS operationQty, " +
	               "       pi.defective AS defectiveQty, " +
	               "       e.equip_name AS equipName, " +
	               "       e.equip_code AS equipCode " +
	               "FROM production_instruct pi " +
	               "JOIN operation_info oi ON pi.operation_id = oi.operation_id " +
	               "LEFT JOIN members m ON oi.member_id = m.id " + 
	               "LEFT JOIN equipment e ON pi.equip_id = e.equip_id " + 
	               "WHERE pi.instruct_code = :instructCode", nativeQuery = true)
	ProcessStepDetailMapping findStepDetailByInstructCode(@Param("instructCode") String instructCode);

	interface ProcessStepDetailMapping {
	    String getOperationName();
	    String getOperationId();
	    String getMemberId();
	    String getMemberName();
	    Integer getOperationQty();
	    Integer getDefectiveQty();
	    String getEquipName();
	    String getEquipCode();
	}
	
	
}
