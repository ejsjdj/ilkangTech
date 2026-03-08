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


    @Query("SELECT p FROM ProcessEntity p " +
            "WHERE (:processId IS NULL OR p.id = :processId)" +
            "AND (:processName IS NULL OR p.name LIKE :processName) "
            )
    Page<ProcessEntity> findByProcessId(@Param("processId") Long processId,
                                        @Param(value = "processName") String processName,
                                        Pageable pageable);
}
