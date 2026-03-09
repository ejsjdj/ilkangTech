package com.itwillbs.ilkwangtech.quality.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.quality.dto.DisposalHistoryDto;
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
	
	@Modifying
	@Query(value = 
	    "INSERT INTO DISPOSAL_HISTORY (id, lot_no, item_code, proc_code, worker_id, disposal_qty, disposal_reason, work_date) " +
	    "SELECT (SELECT NVL(MAX(id), 0) + 1 FROM DISPOSAL_HISTORY), " +
	    "       pi.INSTRUCT_CODE, " +
	    "       i.item_code, " +
	    "       oi.name, " +  /* operation_info 테이블의 name(공정명)을 가져옴 */
	    "       pw.member_id, " +
	    "       pw.defective_qty, " +
	    "       qrr.reject_reason, " +
	    "       SYSDATE " +
	    "FROM production_worker pw " +
	    "JOIN production_instruct pi ON pw.instruct_id = pi.id " +
	    "JOIN item i ON pw.item_id = i.item_id " +
	    "JOIN operation_info oi ON pw.process_id = oi.id " + /* 추가된 JOIN 부분 */
	    "LEFT JOIN qc_reject_reason qrr ON pw.id = qrr.worker_id " +
	    "WHERE pw.id = :workerId", 
	    nativeQuery = true)
	void insertDisposalHistory(@Param("workerId") Long workerId);
	
	@Query(value = 
	        "SELECT " +
	        "   dh.id AS id, " +
	        "   dh.lot_no AS lotNo, " +
	        "   i.item_name AS itemName, " +
	        "   dh.proc_code AS processName, " +
	        "   m.name AS memberName, " +
	        "   dh.disposal_qty AS disposalQty, " +
	        "   dh.disposal_reason AS disposalReason, " +
	        "   TO_CHAR(dh.work_date, 'YYYY-MM-DD HH24:MI:SS') AS workDate " +
	        "FROM DISPOSAL_HISTORY dh " +
	        "LEFT JOIN item i ON dh.item_code = i.item_code " +
	        "LEFT JOIN members m ON dh.worker_id = m.id " +
	        "ORDER BY dh.work_date DESC", 
	        nativeQuery = true)
	    List<DisposalHistoryDto> findDisposalList();
}
