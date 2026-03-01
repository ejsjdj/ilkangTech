package com.itwillbs.ilkwangtech.inventorymg.repository;

import com.itwillbs.ilkwangtech.inventorymg.entity.InventoryHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistoryEntity, Long> {
    
    // 특정 날짜(오늘)의 특정 타입(IN/OUT) 처리 건수 조회
    @Query("SELECT COUNT(h) FROM InventoryHistoryEntity h WHERE h.transactionDate = :today AND h.transactionType = :type")
    long countProcessedToday(@Param("today") LocalDate today, @Param("type") String type);
    
    // 특정 날짜 이후의 모든 수불 이력 조회 (차트 렌더링용)
    List<InventoryHistoryEntity> findByTransactionDateAfter(LocalDate startDate);

}