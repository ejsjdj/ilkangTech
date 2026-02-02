package com.itwillbs.ilkwangtech.hr.entity;


import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 발령 테이블
@Entity
@Table(name = "appointment")
@Getter
@Setter
public class AppointmentEntity {

    public AppointmentEntity() {}

    public AppointmentEntity(Member member) {
        this.memberId = member;
    }

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

    @Column(name = "pre_dept", nullable = true)
    private int preDept; // 이전 부서

    @Column(name = "current_dept", nullable = true)
    private int currentDept; // 현재 부서

    @Column(name = "pre_rank", nullable = true)
    private int preRank; // 이전 직급

    @Column(name = "current_rank", nullable = true)
    private int currentRank; // 현재 직급

    @Column(name = "work_status", nullable = true)
    private String workStatus; // 근무 상태

    @Column(name = "appointment_date", nullable = true)
    private LocalDate appointmentDate; // 발령일

    // 부서 변경 (현재 부서 -> 이전 부서)
    public void changeDept(int currentDept){
        this.preDept = currentDept;
        this.appointmentDate = LocalDate.now();
    }

    // 직급 변경 (현재 직급 -> 이전 직급)
    public void changeRank(int currentRank){
        this.preRank = currentRank;
        this.appointmentDate = LocalDate.now();
    }

    // 근무상태 변경
    public void changeStatus(String workStatus){
        this.workStatus = workStatus;
    }

    // 새로운 부서 등록
    public void newDept(int newDept){
        this.currentDept = newDept;
        this.appointmentDate = LocalDate.now();
    }

    // 새로운 직급 등록
    public void newRank(int newRank){
        this.currentRank = newRank;
        this.appointmentDate = LocalDate.now();
    }

    // 승인 날짜 등록
    public void newDate(LocalDate newDate){
        this.appointmentDate = LocalDate.now();
    }

    // 승인자 등록
    public void newApprover(Member approver){
        this.approverId = approver;
    }
}
