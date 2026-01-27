package com.itwillbs.ilkwangtech.Hr.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long leaveId;

    // 부서별, 전직원
    @OneToOne
    @JoinColumn(name = "common_id")
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

    public void calculateRemainLeave(){
        this.remainLeave = this.totalLeave - this.usedLeave;
    }
}