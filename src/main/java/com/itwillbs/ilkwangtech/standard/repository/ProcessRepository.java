package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {


    // 공정코드 검색
    @Query("""
    SELECT p FROM ProcessEntity p
    WHERE (:keyword IS NULL OR
           p.operationCode LIKE CONCAT('%', :keyword, '%')
           OR p.name LIKE CONCAT('%', :keyword, '%'))
    """)
    Page<ProcessEntity> findByKeyword(@Param("keyword") String keyword,
                                      Pageable pageable);

    // 공정코드 검색(비활성 제외)
    @Query("""
    SELECT p FROM ProcessEntity p
    WHERE p.status = 'ACTIVE'
    """)
    List<ProcessEntity> findByActive();
}
