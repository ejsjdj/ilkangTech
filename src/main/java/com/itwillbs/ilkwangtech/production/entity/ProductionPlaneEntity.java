package com.itwillbs.ilkwangtech.production.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.standard.entity.ProcessRouteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.ibatis.annotations.One;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 생산 계획 엔티티
@Entity
@Getter
@Table(name = "production_plane")
public class ProductionPlaneEntity {

    // 생산헤더 ID
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 공정 라우트 ID
    @ManyToOne
    @JoinColumn(name = "operation_route_id")
    private ProcessRouteEntity route;

    // 헤더 -> 상세 조회 전용 필드
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionPlaneDetailEntity> details;

    // 계획 코드
    @Column(name = "plane_code")
    private String planeCode;

    // 계획 일자
    @Column(name = "plane_date")
    private LocalDateTime planeDate;

    // 등록자
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 제품코드
    @Column(name = "item_id")
    private Long item; // 제품ID와 FK로 연결(제품코드, 제품명)

    // 계획 총 수량
    @Column(name = "total_qty")
    private Long totalQty;

    // 생산 상태
    @Column(name = "status")
    private String status;

    // 메모
    @Column(name = "memo")
    private String memo;

}
