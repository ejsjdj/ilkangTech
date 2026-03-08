package com.itwillbs.ilkwangtech.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwillbs.ilkwangtech.quality.dto.QcHistoryDto;
import com.itwillbs.ilkwangtech.quality.entity.QcItem;

public interface QcItemRepository extends JpaRepository<QcItem, Long> {

	@Query(value = 
	        "SELECT " +
	        "   pw.id AS workerId, " +           
	        "   pi.INSTRUCT_CODE AS instructCode, " +
	        "   i.item_name AS itemName, " +
	        "   pw.end_time AS endTime, " +
	        "   m.name AS memberName, " +
	        "   pw.production_qty AS productionQty, " +
	        "   pw.defective_qty AS defectiveQty, " +
	        "   qrr.reject_reason AS rejectReason " + 
	        "FROM production_worker pw " +
	        "JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	        "JOIN ITEM i ON pw.item_id = i.item_id " +
	        "JOIN members m ON pw.member_id = m.id " +
	        "LEFT JOIN qc_reject_reason qrr ON pw.id = qrr.worker_id " +
	        "ORDER BY pw.end_time DESC", 
	        nativeQuery = true)
	    List<QcHistoryDto> findQcHistoryList();
}
