//package com.itwillbs.ilkwangtech.sales.entity;
//
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "purchase_order_header")
//public class PurchaseOrderHeaderEntity {
//
//    @Id
//    @Column(name = "id", length = 10)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // 발주 ID
//
//    @Column(name = "purchase_order_id")
//    private String purchaseOrderId; // 발주 번호
//
//    @Column(name = "name")
//    private String company;// 거래처명
//
//    @Column(name = "company_manager")
//    private String companyManager;// 담당자명
//
//    @Column(name="phone")
//    private String phone;// 전화번호
//
//    @Column(name = "name")
//    private String name;// 발주 담당자
//
//    @Column(name = "status")
//    private String status;// 발주 상태(발주접수, 발주확정, 출하완료, 검수완료, 입고완료)
//
//    @Column(name = "po_date")
//    private String poDate;// 발주 일자
//
//}
