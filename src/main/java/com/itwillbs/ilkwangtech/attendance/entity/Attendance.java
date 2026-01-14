package com.itwillbs.ilkwangtech.attendance.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import com.itwillbs.ilkwangtech.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "IK_ATTENDANCE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicInsert // DB Default 값(SYSDATE 등) 적용을 위해 사용
public class Attendance {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_att_gen")
    @SequenceGenerator(name = "seq_att_gen", sequenceName = "SEQ_ATTENDANCE", allocationSize = 1)
    @Column(name = "att_id")
    private Long id;

    // 사원 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 근무 기준 날짜 (YYYY-MM-DD)
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    // 출근 시간 (Not Null)
    @Column(name = "clock_in", nullable = false)
    private LocalDateTime clockIn;

    // 퇴근 시간 (Null 허용 -> 출근 시점엔 비어있음)
    @Column(name = "clock_out")
    private LocalDateTime clockOut;

    // 외근 (Null 허용)
    @Column(name = "field_work")
    private LocalDateTime fieldWork;

    // 장기 휴식 (Null 허용)
    @Column(name = "long_break")
    private LocalDateTime longBreak;

    // 복귀 (Null 허용)
    @Column(name = "return_time")
    private LocalDateTime returnTime;

    // [비즈니스 로직 메서드]

    // 퇴근 찍기
    public void recordClockOut() {
        this.clockOut = LocalDateTime.now();
    }
    
    // 외근 나가기
    public void recordFieldWork() {
        this.fieldWork = LocalDateTime.now();
    }

    // 복귀 하기
    public void recordReturn() {
        this.returnTime = LocalDateTime.now();
    }

}
