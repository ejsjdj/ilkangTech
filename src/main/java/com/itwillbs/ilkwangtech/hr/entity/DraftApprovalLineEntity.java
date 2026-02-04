package com.itwillbs.ilkwangtech.hr.entity;


import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "draft_approval_line")
@SequenceGenerator(
        name = "draft_approval_line_seq_gen",
        sequenceName = "draft_approval_line_seq",
        initialValue = 100,
        allocationSize = 1
)
public class DraftApprovalLineEntity {

    // 결재 라인 ID
    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_approval_line_seq_gen")
    private long lineId;

    // 결재 양식 종류
    @Column(nullable = false)
    private String draftType;

    // 승인자 ID
    // 사원 고유번호
    @ManyToOne
    @JoinColumn(name="common_id")
    private Member member;

    // 결재 순서
    @Column(nullable = false)
    private long sequence;
}