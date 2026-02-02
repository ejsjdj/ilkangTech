package com.itwillbs.ilkwangtech.hr.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="commute_update_request")
@NoArgsConstructor
public class CommuteUpdateRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_approval_line_seq_gen")
    private Long requestId; // 기본키

//    @OneToOne
//    @JoinColumn(name = "attendance_id")
    @Column(name = "attendance_id", nullable = true)
    private Long attendance; // 근태 ID

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = true)
    private Member approver; // 승인자

    @Column(name = "req_context", nullable = true)
    private String context; // 제목

    @Column(name = "req_context_detail", nullable = true)
    private String contextDetail; // 상세내용

    @Column(name = "target_date", nullable = true)
    private LocalDate targetDate; // 대상 날짜

    @Column(name = "allow_date", nullable = true)
    private LocalDate allowDate; // 승인 날짜

//    @Builder
//    public void setRequestData(Long attendance, Member approver, String context, String contextDetail,  LocalDate targetDate, LocalDate allowDate){
//        this.attendance = attendance;
//        this.approver = approver;
//        this.context = context;
//        this.contextDetail = contextDetail;
//        this.targetDate = targetDate;
//        this.allowDate = allowDate;
//    }

    // 요청자 입력 set
    public void setNewDate(Long attendance, String context, String contextDetail, LocalDate targetDate){
        this.attendance = attendance;
        this.context = context;
        this.contextDetail = contextDetail;
        this.targetDate = targetDate;
    }

}
