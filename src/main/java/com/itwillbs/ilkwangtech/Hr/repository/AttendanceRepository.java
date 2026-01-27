package com.itwillbs.ilkwangtech.Hr.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.Hr.entity.Attendance;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	// 사원 번호와 날짜(오늘)로 근태 기록 조회
    Optional<Attendance> findByMemberIdAndWorkDate(Long memberId, LocalDate workDate);

    // 사원 번호로 전체 출퇴근 현황 조회
    List<Attendance> findByMemberId(Long userId);

    // 전 사원 근무 현황 조회
    @Query("""
            SELECT a 
            FROM Attendance a
            JOIN FETCH a.member m
            JOIN FETCH m.department d
            """)
    List<Attendance> findAllWithMemberAndDepartments();

    // 사원 번호로 출근 상태 업데이트

}
