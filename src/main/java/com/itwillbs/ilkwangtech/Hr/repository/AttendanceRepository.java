package com.itwillbs.ilkwangtech.Hr.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.Hr.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	// 사원 번호와 날짜(오늘)로 근태 기록 조회
    Optional<Attendance> findByMemberIdAndWorkDate(Long memberId, LocalDate workDate);

}
