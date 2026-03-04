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
	
	@Query(value = "SELECT l.lot_id AS lotId, i.item_name AS itemName, l.lot_type AS lotType, " + // lotType 추가
            "p.instruct_code AS instructCode, p.instruct_qty AS instructQty, p.status AS status, " +
            "p.start_date AS startDate, p.end_date AS endDate, p.defective AS defective " +
            "FROM lot_master l " +
            "JOIN item i ON l.product_id = i.item_id " +
            "LEFT JOIN production_instruct p ON l.lot_id = p.lot_id " +
            "WHERE l.lot_id = :lotId", nativeQuery = true)
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
	}
}
