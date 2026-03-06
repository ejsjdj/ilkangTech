package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionInsturctRepository extends JpaRepository<ProductionInstructEntity, Long> {

    @Query("""
            SELECT p FROM ProductionInstructEntity p
            WHERE :keyword IS NULL OR p.instructCode LIKE CONCAT('%', :keyword, '%')
            """)
    Page<ProductionInstructEntity> findByKeyword(Pageable pageable,
                                                 @Param("keyword") String keyword);

    // 작업지시 특정 공정 작업 완료
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE ProductionInstructEntity p " +
//            "SET p.status = 'COM' " +
//            "WHERE p.instructCode LIKE CONCAT('%', :instructCode, '%')" +
//            "ANDp.process.id = :processId")
//    int updateInstructCompleteStatus(@Param("planeId") String instructCode,
//                                     @Param("processId") Long processId);


    // 불량 등록
//    @Modifying
//    @Query("""
//            UPDATE ProductionInstructEntity p
//            SET p.defective = :defectiveQty
//            WHERE p.instructCode LIKE CONCAT('%', :instructCode, '%')
//            AND p.process.id = :processId
//            """)
//    int updateInstructDefectiveQty(@Param("defectiveQty") Long defectiveQty,
//                                   @Param("instructCode") String instructCode,
//                                   @Param("processId") Long processId
//    );
    
    // 예약재고(작업지시) 총 수량 조회
    @Query("SELECT COALESCE(SUM(p.instructQty), 0) FROM ProductionInstructEntity p WHERE p.item = :itemId AND p.status NOT IN ('COM', 'CAN')")
    Long sumReservedQtyByItemId(@Param("itemId") Long itemId);
    
    // 전체 예약재고량 그룹화 조회 (item이 단순 숫자형)
    @Query("SELECT p.item.itemId, COALESCE(SUM(p.instructQty), 0) FROM ProductionInstructEntity p WHERE p.status NOT IN ('COM', 'CAN') GROUP BY p.item.itemId")
    List<Object[]> sumReservedQtyGrouped();
    
    // 메인 생산계획 수량 + 공정별 추가 생산 수량(addition_qty) 합산 네이티브 쿼리
    @Query(value = 
            "SELECT target.instruct_code, target.item_id, target.item_name, COALESCE(SUM(target.req_qty), 0) " +
            "FROM ( " +
            "    SELECT i.instruct_code, p.item_id, p.item_name, (COALESCE(i.instruct_qty, 0) * b.require_qty) AS req_qty " +
            "    FROM production_instruct i " +
            "    JOIN bom b ON i.item_id = b.child_item_id " +
            "    JOIN item p ON b.parent_item_id = p.item_id " +
            "    WHERE UPPER(i.status) IN ('PROGRESS', 'COMPLETE') " +
            "    " +
            "    UNION ALL " +
            "    " +
            "    SELECT i.instruct_code, p.item_id, p.item_name, (w.addition_qty * b.require_qty) AS req_qty " +
            "    FROM production_instruct i " +
            "    JOIN production_worker w ON i.id = w.instruct_id " +
            "    JOIN bom b ON w.item_id = b.child_item_id " +
            "    JOIN item p ON b.parent_item_id = p.item_id " +
            "    WHERE UPPER(i.status) IN ('PROGRESS', 'COMPLETE') " +
            "      AND w.addition_qty IS NOT NULL " +
            "      AND w.addition_qty > 0 " +
            ") target " +
            "GROUP BY target.instruct_code, target.item_id, target.item_name " +
            "HAVING COALESCE(SUM(target.req_qty), 0) > 0", 
            nativeQuery = true)
     List<Object[]> findMaterialOutboundList();

    // 작업지시 상태 변경
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductionInstructEntity p SET p.status = :status WHERE p.instructCode = :instructCode")
    int updateInstructStatus(@Param("instructCode") String instructCode, @Param("status") String status);
}
