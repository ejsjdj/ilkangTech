package com.itwillbs.ilkwangtech.production.repository;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
