package com.itwillbs.ilkwangtech.hr.entity;


import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "draft_approval_status")
@SequenceGenerator(
        name = "draft_status_seq_gen",
        sequenceName = "draft_status_seq",
        initialValue = 1,
        allocationSize = 1
)
public class DraftApproveStatusEntity {

    // 결재 상태 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draft_status_seq_gen")
    private long statusId;

    // 어떤 문서를 결재하는가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draft_id")
    private DraftEntity draftEntity;

    // 누가 결재하는가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 이 사람의 결재 순서는?
    @Column(nullable = false)
    private long sequence;

    // 이 사람의 결재 상태는?
    @Column(nullable = false)
    private String status;
}
