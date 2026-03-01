package com.itwillbs.ilkwangtech.sales.repository;

import com.itwillbs.ilkwangtech.sales.entity.PurchaseRequestHeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRequestHeaderRepository extends JpaRepository<PurchaseRequestHeaderEntity, Long> {

    @Query("""
        select distinct h 
        from PurchaseRequestHeaderEntity h
        left join fetch h.lines
        where h.id = :id
    """)
    Optional<PurchaseRequestHeaderEntity> findDetailById(@Param("id") Long id);

}
