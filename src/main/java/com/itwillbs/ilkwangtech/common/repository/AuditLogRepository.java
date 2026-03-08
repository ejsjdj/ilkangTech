package com.itwillbs.ilkwangtech.common.repository;

import com.itwillbs.ilkwangtech.common.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEmployeeNumberOrderByCreatedAtDesc(String employeeNumber);
    List<AuditLog> findByEntityNameAndEntityIdOrderByCreatedAtDesc(String entityName, String entityId);
    List<AuditLog> findAllByOrderByCreatedAtDesc();
}
