package com.itwillbs.ilkwangtech.common.service;

import java.util.List;
import com.itwillbs.ilkwangtech.common.entity.AuditLog;
import com.itwillbs.ilkwangtech.common.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findAll() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByEmployeeNumber(String employeeNumber) {
        return auditLogRepository.findByEmployeeNumberOrderByCreatedAtDesc(employeeNumber);
    }
}
