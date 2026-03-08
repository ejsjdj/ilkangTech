package com.itwillbs.ilkwangtech.common.service;

import java.util.List;
import com.itwillbs.ilkwangtech.common.entity.AuditLog;

public interface AuditLogService {
    void save(AuditLog auditLog);
    List<AuditLog> findAll();
    List<AuditLog> findByEmployeeNumber(String employeeNumber);
}
