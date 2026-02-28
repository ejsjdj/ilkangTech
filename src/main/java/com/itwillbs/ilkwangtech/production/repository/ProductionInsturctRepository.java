package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
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
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductionInstructEntity p " +
            "SET p.status = 'COM' " +
            "WHERE p.instructCode LIKE CONCAT('%', :instructCode, '%')" +
            "AND p.process = :processId")
    int updateInstructCompleteStatus(@Param("planeId") String instructCode,
                                     @Param("processId") Long processId);
    // 재고 수량 업데이트


    // 불량 등록
    @Modifying
    @Query("""
            UPDATE ProductionInstructEntity p
            SET p.defective = :defectiveQty
            WHERE p.instructCode LIKE CONCAT('%', :instructCode, '%')
            AND p.process = :processId
            """)
    int updateInstructDefectiveQty(@Param("defectiveQty") Long defectiveQty,
                                   @Param("instructCode") String instructCode,
                                   @Param("processId") Long processId
    );
}
