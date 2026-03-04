package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {


    @Query("""
    SELECT p FROM ProcessEntity p
    WHERE (:keyword IS NULL OR
           p.operationCode LIKE CONCAT('%', :keyword, '%')
           OR p.name LIKE CONCAT('%', :keyword, '%'))
""")
    Page<ProcessEntity> findByKeyword(@Param("keyword") String keyword,
                                      Pageable pageable);
}
