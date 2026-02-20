//package com.itwillbs.ilkwangtech.sales.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//// 발주 엔티티
//@Entity
//@Getter
//@Setter
//@Table(name = "purchase_order")
//public class PurchaseOrderEntity {
//
//    @Id
//    @Column(name = "id", length = 10)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // 발주 ID
//
//    // 발주 코드
//    @ManyToOne
//    @JoinColumn(name = "purchase_order_id")
//    private PurchaseOrderHeaderEntity purchaseOrderHeaderEntity;
//
//    // 구매요청 코드
//    //private PurchaseRequestDetailEntity purchaseRequestDetailEntity;
//
//    // 요청일
//
//
//    // 납기일
//    // 납품주소
//
//    // 품목명
//    // 수량
//    // 단가
//    // 합계
//}