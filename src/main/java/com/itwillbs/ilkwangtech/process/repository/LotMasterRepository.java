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
	
	@Query(value = "SELECT lm.lot_id AS lotId, NVL(i.item_name, '제품명 미등록') AS itemName, " +
	        "NVL(lm.quantity, 0) AS quantity, lm.status AS status " +
	        "FROM ( " +
	        "    SELECT lot_id, product_id, quantity, status, lot_type, LEVEL as lvl " +
	        "    FROM LOT_MASTER " +
	        "    START WITH TRIM(lot_id) = TRIM(:rawLotId) " +
	        "    CONNECT BY PRIOR TRIM(lot_id) = TRIM(parent_lot_id) " +
	        ") lm " +
	        "LEFT JOIN item i ON TRIM(lm.product_id) = TRIM(i.item_code) " +
	        "WHERE lm.lvl > 1", nativeQuery = true)
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
	

	@Query(value = "SELECT oi.description AS operationName, " +
	            "       oi.operation_id AS operationId, " +
	            "       oi.member_id AS memberId, " +
	            "       m.name AS memberName, " +
	            "       pi.operation_qty AS operationQty, " +
	            "       pi.defective AS defectiveQty, " +
	            "       '-' AS equipName, " +
	            "       '-' AS equipCode " +
	            "FROM production_instruct pi " +
	            "JOIN operation_info oi ON pi.operation_id = oi.operation_id " +
	            "LEFT JOIN members m ON oi.member_id = m.id " + 
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
	
	// 원자재 Summary Box 표시 정보
	interface RawMaterialDetailMapping {
	    String getLotId();
	    String getItemCode();
	    String getItemName();
	    String getUom();
	    String getItemType();
	    Integer getCurrentQuantity();
	    LocalDateTime getExpirationDate();
	    String getZone();
	}

	@Query(value = "SELECT iv.lot_number AS lotId, it.item_code AS itemCode, it.item_name AS itemName, " +
	        "it.uom AS uom, CAST(it.item_type AS VARCHAR(10)) AS itemType, " +
	        "iv.current_quantity AS currentQuantity, iv.expiration_date AS expirationDate, iv.zone AS zone " +
	        "FROM inventory iv " +
	        "JOIN item it ON iv.item_id = it.item_id " +
	        "WHERE iv.lot_number = :lotId", nativeQuery = true)
	RawMaterialDetailMapping findRawMaterialDetail(@Param("lotId") String lotId);
	
	// 아코디언 클릭 시 매핑
	interface SubDetailMapping {
	    String getLotId();
	    String getItemId();
	    String getItemName();
	    String getItemType();
	    String getInstructCode();
	    Integer getProductionQty();
	    Integer getDefectiveQty();
	    String getStartTime();
	    String getEndTime();
	}

	@Query(value = "SELECT lm.lot_id AS lotId, " +
	        "i.item_code AS itemId, " + 
	        "NVL(i.item_name, '제품명 미등록') AS itemName, " +
	        "CAST(i.item_type AS VARCHAR2(10)) AS itemType, " +
	        "pi.instruct_code AS instructCode, " +
	        "NVL(pw.production_qty, NVL(lm.quantity, 0)) AS productionQty, " +
	        "NVL(pw.defective_qty, 0) AS defectiveQty, " +
	        "CAST(pw.start_time AS VARCHAR2(50)) AS startTime, " +
	        "CAST(pw.end_time AS VARCHAR2(50)) AS endTime " +
	        "FROM LOT_MASTER lm " +
	        "LEFT JOIN item i ON TRIM(lm.product_id) = TRIM(i.item_code) " +
	        "LEFT JOIN production_worker pw ON TRIM(lm.lot_id) = TRIM(pw.lot_id) " +
	        "LEFT JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	        "WHERE TRIM(lm.lot_id) = TRIM(:lotId)", nativeQuery = true)
	SubDetailMapping findSubDetailByLotId(@Param("lotId") String lotId);
	
	@Query(value = "SELECT pw.lot_id AS lotId, i.item_name AS itemName, " +
	        "CAST(pw.end_time AS TIMESTAMP) AS createdDate, " +
	        "CAST(i.item_type AS VARCHAR2(10)) AS lotType, 'DONE' AS status " +
	        "FROM production_worker pw " +
	        "LEFT JOIN item i ON TRIM(pw.item_id) = TRIM(i.item_id) " + // item_id를 기준으로 조인
	        "WHERE pw.status = 'COMPLETE' " +
	        "ORDER BY pw.end_time DESC", nativeQuery = true)
	List<LotSummaryMapping> findAllProdAndSemiLotsFromWorker();
	
	interface ProdLotDetailMapping {
	    String getLotId();
	    String getItemCode();
	    String getItemName();
	    String getInstructCode();
	    Integer getProductionQty();
	    Integer getDefectiveQty();
	    String getEndTime();
	}
	
	@Query(value = "SELECT pw.lot_id AS lotId, i.item_code AS itemCode, NVL(i.item_name, '제품명 미등록') AS itemName, " +
	        "pi.instruct_code AS instructCode, NVL(pw.production_qty, 0) AS productionQty, " +
	        "NVL(pw.defective_qty, 0) AS defectiveQty, CAST(pw.end_time AS VARCHAR2(50)) AS endTime " +
	        "FROM production_worker pw " +
	        "LEFT JOIN item i ON TRIM(pw.item_id) = TRIM(i.item_id) " +
	        "LEFT JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	        "WHERE TRIM(pw.lot_id) = TRIM(:lotId)", nativeQuery = true)
	ProdLotDetailMapping findProdLotDetail(@Param("lotId") String lotId);

	interface ProdSubDetailMapping {
	    String getOperationName();
	    String getOperationId();
	    String getMemberName();
	    String getEmployeeNumber();
	    Integer getProductionQty();
	    Integer getDefectiveQty();
	    String getEquipName();
	    String getEquipCode();
	}
	
	@Query(value = "SELECT oi.description AS operationName, oi.operation_id AS operationId, " +
	        "m.name AS memberName, m.employee_number AS employeeNumber, " +
	        "NVL(pw.production_qty, 0) AS productionQty, NVL(pw.defective_qty, 0) AS defectiveQty, " +
	        "'-' AS equipName, '-' AS equipCode " +
	        "FROM production_worker pw " +
	        "LEFT JOIN operation_info oi ON pw.process_id = oi.id " +
	        "LEFT JOIN members m ON pw.member_id = m.id " +
	        "LEFT JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	        "WHERE TRIM(pw.lot_id) = TRIM(:lotId)", nativeQuery = true)
	ProdSubDetailMapping findProdSubDetailByLotId(@Param("lotId") String lotId);
	
	// 해당 LOT의 공정 목록 및 상세 정보 매핑
	interface ProdProcessMapping {
	    String getOperationName();
	    String getOperationId();
	    String getMemberName();
	    String getEmployeeNumber();
	    Integer getProductionQty();
	    Integer getDefectiveQty();
	    String getEquipName();
	    String getEquipCode();
	    String getStatus();
	}

	@Query(value = "SELECT oi.description AS operationName, oi.operation_id AS operationId, " +
	        "m.name AS memberName, m.employee_number AS employeeNumber, " +
	        "NVL(pw.production_qty, 0) AS productionQty, NVL(pw.defective_qty, 0) AS defectiveQty, " +
	        "'-' AS equipName, '-' AS equipCode, pw.status AS status " +
	        "FROM production_worker pw " +
	        "LEFT JOIN operation_info oi ON pw.process_id = oi.id " +
	        "LEFT JOIN members m ON pw.member_id = m.id " +
	        "LEFT JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	        "WHERE TRIM(pw.lot_id) = TRIM(:lotId) " +
	        "ORDER BY pw.end_time ASC", nativeQuery = true)
	List<ProdProcessMapping> findProcessesByLotId(@Param("lotId") String lotId);


	@Query(value = "SELECT lm.lot_id AS lotId, NVL(i.item_name, '제품명 미등록') AS itemName, " +
	        "NVL(lm.quantity, 0) AS quantity, lm.status AS status " +
	        "FROM ( " +
	        "    SELECT lot_id, product_id, quantity, status, LEVEL as lvl " +
	        "    FROM LOT_MASTER " +
	        "    START WITH TRIM(lot_id) = TRIM(:lotId) " +
	        "    CONNECT BY PRIOR TRIM(parent_lot_id) = TRIM(lot_id) " +
	        ") lm " +
	        "LEFT JOIN item i ON TRIM(lm.product_id) = TRIM(i.item_code) " +
	        "WHERE lm.lvl > 1 AND lm.lot_id LIKE 'RW-%'", nativeQuery = true)
	List<FinishedUsageMapping> findRecursiveMaterialsByLotId(@Param("lotId") String lotId);
	
}
