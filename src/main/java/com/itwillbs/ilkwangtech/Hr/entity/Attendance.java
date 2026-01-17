package com.itwillbs.ilkwangtech.Hr.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.itwillbs.ilkwangtech.Hr.constant.AttendanceStatus;
import com.itwillbs.ilkwangtech.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "IK_ATTENDANCE")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "member")
public class Attendance {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_attendance_gen")
    @SequenceGenerator(name = "seq_attendance_gen", sequenceName = "SEQ_ATTENDANCE", allocationSize = 1)
    @Column(name = "attendance_id")
    private Long id;

    // 직원 정보 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 근무 날짜
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    // 출근 시간
    @Column(name = "in_time")
    private LocalDateTime inTime;

    // 퇴근 시간
    @Column(name = "out_time")
    private LocalDateTime outTime;
    
    // 외근 시작 시간
    @Column(name = "go_out_time")
    private LocalDateTime goOutTime; 

    // 복귀 시간
    @Column(name = "return_time")
    private LocalDateTime returnTime; 

    // 근무 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private AttendanceStatus status;

    // 상세 메모
    @Column(name = "memo", length = 500)
    private String memo;

    // 1. 퇴근 처리
    public void recordClockOut(LocalDateTime time) {
        this.outTime = time;
        this.status = AttendanceStatus.OFF_DUTY;
    }

    // 2. 상태 변경
    public void changeStatus(AttendanceStatus newStatus) {
        this.status = newStatus;
    }
    
    // 3. 외근 정보 업데이트 (상태 변경 포함)
    public void updateOutsideInfo(AttendanceStatus newStatus, String memo) {
        this.status = newStatus;
        this.memo = memo;
        this.goOutTime = LocalDateTime.now(); 
    }
    
    // 4. 복귀 처리 메서드
    public void recordReturn() {
        this.status = AttendanceStatus.ON_DUTY;
        this.returnTime = LocalDateTime.now(); 
    }

}
