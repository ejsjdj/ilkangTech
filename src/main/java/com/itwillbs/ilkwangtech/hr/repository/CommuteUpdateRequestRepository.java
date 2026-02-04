package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.entity.CommuteUpdateRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommuteUpdateRequestRepository extends JpaRepository<CommuteUpdateRequestEntity, String> {

}
