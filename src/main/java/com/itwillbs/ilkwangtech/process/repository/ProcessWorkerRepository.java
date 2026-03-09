package com.itwillbs.ilkwangtech.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;

import jakarta.transaction.Transactional;

public interface ProcessWorkerRepository extends JpaRepository<ProductionWorkerEntity, Long> {

	@Modifying
    @Transactional
    @Query(value = "UPDATE production_worker SET start_time = SYSDATE, status = 'PROGRESS' WHERE id = :workerId", nativeQuery = true)
    void updateStartTime(@Param("workerId") Long workerId);

    // 2. 종료 버튼: end_time 및 생성된 lot_id 업데이트
    @Modifying
    @Transactional
    @Query(value = "UPDATE production_worker SET end_time = SYSDATE, status = 'COMPLETE', lot_id = :lotId WHERE id = :workerId", nativeQuery = true)
    void updateEndTimeAndLotId(@Param("workerId") Long workerId, @Param("lotId") String lotId);

    // 3. 작업자 및 공정 기본 정보 조회용 매핑
    @Query(value = "SELECT w.id AS workerId, w.instruct_id AS instructId, w.production_qty AS productionQty, " +
                   "o.name AS processName, p.instruct_code AS instructCode, i.item_code AS itemCode " +
                   "FROM production_worker w " +
                   "JOIN production_instruct p ON w.instruct_id = p.id " +
                   "JOIN operation_info o ON w.process_id = o.id " +
                   "JOIN item i ON p.item_id = i.item_id " +
                   "WHERE w.id = :workerId", nativeQuery = true)
    WorkerInfoMapping findWorkerInfoById(@Param("workerId") Long workerId);

    interface WorkerInfoMapping {
        Long getWorkerId();
        Long getInstructId();
        Integer getProductionQty();
        String getProcessName();
        String getInstructCode();
        String getItemCode();
    }

    // 4. 이전 공정의 LOT ID 찾기 (PARENT_LOT_ID)
    @Query(value = "SELECT lot_id FROM (" +
                   "  SELECT lot_id FROM production_worker " +
                   "  WHERE instruct_id = :instructId AND id < :workerId AND lot_id IS NOT NULL " +
                   "  ORDER BY id DESC" +
                   ") WHERE ROWNUM = 1", nativeQuery = true)
    String findParentLotId(@Param("instructId") Long instructId, @Param("workerId") Long workerId);

    // 5. 오늘 날짜 기준 동일 제품의 시퀀스 구하기 (LOT 번호 채번)
    @Query(value = "SELECT NVL(MAX(TO_NUMBER(SUBSTR(lot_id, -3))), 0) + 1 " +
                   "FROM lot_master " +
                   "WHERE product_id = :itemCode AND TO_CHAR(created_date, 'YYYYMMDD') = TO_CHAR(SYSDATE, 'YYYYMMDD')", nativeQuery = true)
    Integer getNextLotSequence(@Param("itemCode") String itemCode);

    // 6. lot_master 테이블 Insert
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO lot_master (lot_id, lot_type, parent_lot_id, product_id, quantity, created_date, status) " +
                   "VALUES (:lotId, :lotType, :parentLotId, :productId, :quantity, SYSDATE, 'DONE')", nativeQuery = true)
    void insertLotMaster(@Param("lotId") String lotId, @Param("lotType") String lotType, 
                         @Param("parentLotId") String parentLotId, @Param("productId") String productId, 
                         @Param("quantity") Integer quantity);
    
    @Query(value = "SELECT lot_id FROM production_worker " +
	            "WHERE instruct_id = :instructId AND lot_id LIKE 'SAM-%' AND ROWNUM = 1", nativeQuery = true)
	String findExistingSamLotId(@Param("instructId") Long instructId);
}
