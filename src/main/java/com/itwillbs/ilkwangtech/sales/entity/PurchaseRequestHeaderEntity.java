package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

// 구매요청 헤더 엔티티
@Entity
@Getter // 추가
@Setter // 추가
@Table(name = "purchase_request_header")
public class PurchaseRequestHeaderEntity {

    // 구매요청 헤더 ID
    @Id
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 헤더 -> 라인 조회용 필드
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseRequestEntity> lines;

    // 구매요청 코드
    @Column(name = "purchase_request_code", unique = true)
    private String purchaseRequestCode;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 요청자 ID

    @Column(name = "request_date")
    private LocalDate requestDate; // 등록일

    @Column(name = "due_date")
    private LocalDate dueDate; // 납기일

    @Column(name = "delivery_locate")
    private String deliveryLocate; // 납품장소

    @Column(name = "contract_type")
    private String contractType; // 계약 구분

    @Column(name = "produce_type")
    private String produceType; // 제작 구분

}
