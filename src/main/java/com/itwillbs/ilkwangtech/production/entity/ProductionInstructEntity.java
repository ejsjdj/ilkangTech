package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import jakarta.persistence.*;
import lombok.Getter;

// 생산지시 엔티티
@Entity
@Getter
@Table(name = "production_instruct")
public class ProductionInstructEntity {

    // 생산지시 ID
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 생산지시 코드(?공정마다 지시코드가 다름?)
    @Column(name = "instruct_code")
    private String instructCode;

    // 생산계획 ID
    @ManyToOne
    @JoinColumn(name = "production_id")
    private ProductionPlaneEntity productionId;

    // 생산계획 상세 ID
    @ManyToOne
    @JoinColumn(name = "detail_id")
    private ProductionPlaneDetailEntity detailId;

    // 공정 ID
    @ManyToOne
    @JoinColumn(name = "operation_id")
    private ProcessEntity process;

    // 지시 수량
    @Column(name = "instruct_qty")
    private Long instructQty;

    // 담당자
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 상태
    @Column(name = "status")
    private String status;

    // 불량
    @Column(name = "defective")
    private Long defective;
}
