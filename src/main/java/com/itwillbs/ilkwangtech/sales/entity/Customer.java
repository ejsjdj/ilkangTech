package com.itwillbs.ilkwangtech.sales.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "CUSTOMER_SEQ_GENERATOR",
        sequenceName = "CUSTOMERS_SEQ",
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
    
    @Column(length = 50)
    private String businessType; // 업종
    
    @Column(length = 20)
    private String status = "ACTIVE"; // 상태 (기본값: ACTIVE)
    
    @Column(length = 50)
    private String managerPhone; // 담당자 연락처
    
    @Column(length = 100)
    private String managerEmail; // 담당자 이메일
    
    @Column(length = 255)
    private String website; // 웹사이트
    
    @Column(length = 50)
    private String createdBy; // 등록자
    
    @Column(length = 50)
    private String updatedBy; // 수정자
    
    @Column(updatable = false)
    private LocalDateTime createdAt; // 등록일
    
    private LocalDateTime updatedAt; // 수정일
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
