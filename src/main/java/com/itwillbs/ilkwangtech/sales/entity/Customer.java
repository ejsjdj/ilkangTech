package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "CUSTOMER_SEQ_GENERATOR",
        sequenceName = "CUSTOMER_SEQ",
        initialValue = 1001,
        allocationSize = 1
)
/**
 * 고객 정보를 저장하는 엔티티
 *
 */
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_SEQ_GENERATOR")
    private Long customerId;

    @Column(nullable = false, length = 100)
    private String customerName; // 업체명

    @Column(length = 20)
    private String businessNumber; // 사업자번호

    @Column(length = 50)
    private String representativeName; // 대표자명

    @Column(length = 50)
    private String phoneNumber; // 전화번호

    @Column(length = 50)
    private String faxNumber; // 팩스번호

    @Column(length = 255)
    private String address; // 주소

    @Column(length = 100)
    private String email; // 이메일

    @Column(length = 50)
    private String contactPerson; // 담당자

    @Column(length = 255)
    private String remark; // 비고

}
