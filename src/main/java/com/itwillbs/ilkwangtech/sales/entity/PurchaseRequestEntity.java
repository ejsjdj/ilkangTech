package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 구매요청 엔티티
@Entity
@Getter
@Setter
@Table(name = "purchase_request")
public class PurchaseRequestEntity {

    @Id
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 구매요청 ID

    @Column(name = "purchase_Request_Id")
    private String purchaseRequestId; // 구매코드

    @Column(name = "item_id")
    private Long itemId; // 품목 ID

    @Column(name = "use")
    private String use; // 용도

    @Column(name = "quantity")
    private Long quantity; // 수량

    @Column(name = "unit")
    private String unit; // 단위

    @Column(name = "amount")
    private Long amount; // 총 금액

    @Column(name = "contract_type")
    private String contractType; // 계약 구분

    @Column(name = "produce_type")
    private String produceType; // 제작 구분

    @Column(name = "due_date")
    private LocalDate dueDate; // 납기일

    @Column(name = "delivery_locate")
    private String deliveryLocate; // 납품장소

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 요청자 ID

    @Column(name = "request_date")
    private LocalDate requestDate; // 등록일

}