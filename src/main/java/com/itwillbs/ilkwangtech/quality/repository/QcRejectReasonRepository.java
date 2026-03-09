package com.itwillbs.ilkwangtech.quality.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itwillbs.ilkwangtech.quality.entity.QcRejectReason;

public interface QcRejectReasonRepository extends JpaRepository<QcRejectReason, Long> {
    // workerId로 기존에 등록된 사유가 있는지 검색
    Optional<QcRejectReason> findByWorkerId(Long workerId);
}
