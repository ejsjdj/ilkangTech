package com.itwillbs.ilkwangtech.Hr.entity;


import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "draft_approval_line")
public class DraftApprovalLineEntity {

    // 결재 라인 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String lind_id;

    // 결재 양식 종류
    @Column(nullable = false)
    private String draft_type;

    // 승인자
    @ManyToOne
    @JoinColumn(name="common_id")
    private Member member;

    // 결재 순서
    @Column(nullable = false)
    private int sequence;

    // 승인 상태
    @Column(nullable = false)
    private int status;
}
