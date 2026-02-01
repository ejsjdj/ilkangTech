package com.itwillbs.ilkwangtech.hr.repository;

import com.itwillbs.ilkwangtech.hr.dto.LeaveByDepartmentDTO;
import com.itwillbs.ilkwangtech.hr.dto.LeaveDTO;
import com.itwillbs.ilkwangtech.hr.entity.LeaveEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveEntity, Long> {

    // 개인 휴가 조회
    @Query("SELECT new com.itwillbs.ilkwangtech.hr.dto.LeaveDTO(" +
            "d.draftStartDate, d.draftEndDate, l.totalLeave, l.usedLeave, l.remainLeave) " +
            "FROM LeaveEntity l " +
            "LEFT JOIN DraftEntity d ON l.member = d.member " +
            "AND d.draftType = 'PTO' " +
            "AND d.draftStatus = '승인' " +
            "AND d.draftStartDate <= :endDate " +
            "AND d.draftEndDate >= :startDate " +
            "AND d.draftEndDate >= CAST(:workDate AS date) " +
            "WHERE l.member.id = :userId")
    List<LeaveDTO> findVacationAndLeaveDetails(@Param("userId") long userId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               @Param("workDate") LocalDate workDate);
}

