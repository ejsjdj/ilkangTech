package com.itwillbs.ilkwangtech.sales.entity;


import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import com.itwillbs.ilkwangtech.sales.constant.PurchaseOrderStatus;
import com.itwillbs.ilkwangtech.sales.dto.PurchaseOrderLineDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "purchase_order_header")

public class PurchaseOrderHeaderEntity {

    // 발주헤더 ID
    @Id
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 헤더 -> 라인 조회용 필드
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderEntity> lines;

    @Column(name = "purchase_order_Code")
    private String purchaseOrderCode; // 발주 코드

    @Column(name = "company")
    private String company;// 거래처명

    @Column(name = "company_manager")
    private String companyManager;// 담당자명

    @Column(name = "phone")
    private String phone;// 전화번호

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;// 발주 담당자

    @Column(name = "status")
    private String status;// 발주 상태(발주접수, 발주확정, 출하완료, 검수완료, 입고완료)

    @Column(name = "order_date")
    private LocalDate orderDate;// 발주 일자

    @Column(name = "amount")
    private Long amount; // 총 금액


    // 헤더 필드 저장 메서드
    public static PurchaseOrderHeaderEntity saveHeader(
            String purchaseOrderCode,
            String company,
            String companyManager,
            String phone,
            Member member,

            LocalDate orderDate
            ) {
        PurchaseOrderHeaderEntity header = new PurchaseOrderHeaderEntity();

        header.purchaseOrderCode = purchaseOrderCode;
        header.company = company;
        header.companyManager =companyManager;
        header.phone = phone;
        header.member = member;
        header.orderDate = orderDate;
        header.status = String.valueOf(PurchaseOrderStatus.CONFIRMED);
        // header.amount = amount;

        return header;
    }


    // 라인 필드 저장 메서드
    public void saveLine(PurchaseOrderEntity line) {
        this.lines.add(line);
        line.setHeader(this);
    }
}