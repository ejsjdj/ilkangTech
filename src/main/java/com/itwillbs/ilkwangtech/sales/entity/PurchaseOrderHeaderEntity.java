package com.itwillbs.ilkwangtech.sales.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "purchase_order_header")
public class PurchaseOrderHeaderEntity {

    @Id
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 발주 ID

    // 헤더 -> 라인 조회용 필드
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderEntity> lines;

    @Column(name = "purchase_order_id")
    private String purchaseOrderCode; // 발주 코드

    @Column(name = "company")
    private String company;// 거래처명

    @Column(name = "company_manager")
    private String companyManager;// 담당자명

    @Column(name = "phone")
    private String phone;// 전화번호

    @Column(name = "name")
    private String name;// 발주 담당자

    @Column(name = "status")
    private String status;// 발주 상태(발주접수, 발주확정, 출하완료, 검수완료, 입고완료)

    @Column(name = "order_date")
    private String orderDate;// 발주 일자

    @Column(name = "amount")
    private Long amount; // 총 금액

}