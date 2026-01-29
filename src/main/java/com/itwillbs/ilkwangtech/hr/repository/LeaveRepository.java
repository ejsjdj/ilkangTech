package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveEntity, Long> {

    Optional<LeaveEntity> findByMemberId(long userId);





}
