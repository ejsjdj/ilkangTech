package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.entity.DraftApprovalLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 결재 양식별로 결재라인 가져오기
@Repository
public interface DraftApprovalLineRepository extends JpaRepository<DraftApprovalLineEntity, String> {

    List<DraftApprovalLineEntity> findByDraftType(String type);
}
