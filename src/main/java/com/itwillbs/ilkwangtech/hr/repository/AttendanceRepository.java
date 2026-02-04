package com.itwillbs.ilkwangtech.hr.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.itwillbs.ilkwangtech.member.entity.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	// 사원 번호와 날짜(오늘)로 근태 기록 조회
    Optional<Attendance> findByMemberIdAndWorkDate(Long memberId, LocalDate workDate);

    // 사원 번호로 개인 출퇴근 현황 조회
    List<Attendance> findByMemberId(Long userId);

    // 시작일(startDate)과 종료일(endDate) 사이의 모든 기록을 조회
    List<Attendance> findByMemberIdAndWorkDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    // 부서별 출퇴근/근무 현황 조회
    @Query("SELECT a " +
            "FROM Attendance a JOIN FETCH a.member m " +
            "WHERE m.department = :dept " +
            "AND a.workDate = :date")
    List<Attendance> findByDepartmentAndDate(@Param("dept") Integer dept, @Param("date") LocalDate date);

}