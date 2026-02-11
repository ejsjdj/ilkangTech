package com.itwillbs.ilkwangtech.hr.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// 휴가 현황
@Entity
@Getter
@Setter
@Table(name = "leave_status")
public class LeaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_status_seq_gen")
    @SequenceGenerator(
        name = "leave_status_seq_gen", 
        sequenceName = "leave_status_seq", // DB에 있는 실제 시퀀스 이름 (로그에 뜬 이름)
        allocationSize = 1 // ★ 핵심: DB의 증가값인 1과 일치시켜야 함 (기본값 50 -> 1)
    )
    private long leaveId;

    // 부서별, 전직원
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 총 휴가
    @Column(nullable = true)
    private long totalLeave;

    // 올해 사용 휴가(1월 1일 초기화)
    @Column(nullable = true)
    private long usedLeave;;

    // 잔여휴가
    @Column(nullable = true)
    private long remainLeave;

    public void calculateRemainLeave(){this.remainLeave = this.totalLeave - this.usedLeave;
    }
}