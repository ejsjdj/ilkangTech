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
	
	@Query(value = "SELECT * FROM ( " +
	            "    SELECT lm.lot_id AS lotId, i.item_name AS itemName, lm.lot_type AS lotType, " +
	            "           pi.instruct_code AS instructCode, pi.instruct_qty AS instructQty, " +
	            "           lm.status AS status, lm.created_date AS startDate, pi.end_date AS endDate, " +
	            "           pi.defective AS defective, m.name AS operatorName " +
	            "    FROM lot_master lm " +
	            "    JOIN item i ON lm.product_id = i.item_id " +
	            "    LEFT JOIN production_worker pw ON lm.lot_id = pw.lot_id " +
	            "    LEFT JOIN production_instruct pi ON pw.instruct_id = pi.id " + // [확인] pi.id가 기본키임
	            "    LEFT JOIN members m ON pw.member_id = m.id " + 
	            "    WHERE lm.lot_id = :lotId " +
	            "    ORDER BY pw.id DESC " +
	            ") WHERE ROWNUM = 1", nativeQuery = true)
	LotDetailMapping findLotDetailByLotId(@Param("lotId") String lotId);

	interface LotDetailMapping {
	    String getLotId();
	    String getItemName();
	    String getLotType();
	    String getInstructCode();
	    Integer getInstructQty();
	    String getStatus();
	    LocalDateTime getStartDate();
	    LocalDateTime getEndDate();
	    Integer getDefective();
	    String getOperatorName(); // 추가
	}
	
	@Query(value = "SELECT iv.lot_number AS lotId, it.item_name AS itemName, " +
            "'DONE' AS status, iv.expiration_date AS createdDate, 'M' AS lotType " +
            "FROM inventory iv " +
            "JOIN item it ON iv.item_id = it.item_id", nativeQuery = true)
	List<LotMasterRepository.LotSummaryMapping> findAllInventoryAsLots();
	
	@Query(value = "SELECT lot_id AS lotId, i.item_name AS itemName, " +
	            "quantity AS quantity, status AS status " +
	            "FROM ( " +
	            "    SELECT * FROM lot_master " +
	            "    START WITH lot_id = :rawLotId " + // [수정] 부모ID가 아닌 선택한 LOT_ID부터 시작
	            "    CONNECT BY PRIOR lot_id = parent_lot_id " + 
	            ") lm " +
	            "JOIN item i ON lm.product_id = i.item_id " +
	            "WHERE lm.lot_type = 'F'", nativeQuery = true) // 최종 결과물(F)만 필터링
	List<FinishedUsageMapping> findFinishedLotsByRawLotId(@Param("rawLotId") String rawLotId);
	
	interface FinishedUsageMapping {
	 String getLotId();
	 String getItemName();
	 Integer getQuantity();
	 String getStatus();
	}
	
	@Query(value = "SELECT lotId, itemName, createdDate, lotType, status " +
            "FROM ( " +
            "    /* 1. 완제품(ASSEMBLY): JOIN을 통해 제품명(item_name)을 가져옵니다 */ " +
            "    SELECT a.lot_id AS lotId, i.item_name AS itemName, " +
            "           a.assembly_date AS createdDate, 'F' AS lotType, 'DONE' AS status " +
            "    FROM ASSEMBLY a " +
            "    JOIN LOT_MASTER lm ON a.lot_id = lm.lot_id " +
            "    JOIN item i ON lm.product_id = i.item_id " +
            "    UNION ALL " +
            "    /* 2. 반제품(PART_PRODUCTION): 기존 컬럼명(part_name, production_date) 활용 */ " +
            "    SELECT lot_id AS lotId, part_name AS itemName, " +
            "           production_date AS createdDate, 'S' AS lotType, 'DONE' AS status " +
            "    FROM PART_PRODUCTION " +
            ") " +
            "ORDER BY createdDate DESC", nativeQuery = true)
	List<LotSummaryMapping> findAllProdAndSemiLots();
	
	
	
	
}
