package com.itwillbs.ilkwangtech.Hr.entity;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 발령 테이블
@Entity
@Table(name = "appointment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentEntity {

    /*
    1. 사번
    2. 이전부서
    3. 이전직급
    4. 근무상태
    5. 발령일
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member memberId; // 사원 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = true)
    private Member approverId; // 승인자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_dept_id", nullable = true)
    private Department previousDeptId; // 이전 부서

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nowDept", nullable = true)
    private Member nowDept; // 현재 부서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_position_id", nullable = true)
    private Position previousPositionId; // 이전 직급

    @Column(name = "work_status", nullable = true)
    private String workStatus; // 근무 상태

    @Column(name = "appointment_date", nullable = true)
    private LocalDate appointmentDate; // 발령일

    // 부서 변경
    public void changeDept(Department currentDept){
        this.previousDeptId = currentDept;
        this.appointmentDate = LocalDate.now();
    }

    // 직급 변경
    public void changeRank(){


    }

    // 근무상태 변경
    public void changeStatus(){


    }


}
