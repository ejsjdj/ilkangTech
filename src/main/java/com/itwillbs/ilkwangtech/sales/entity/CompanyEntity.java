package com.itwillbs.ilkwangtech.sales.entity;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.constant.CompanyStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COMPANY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "COMPANY_CODE", nullable = false, length = 50)
    private String companyCode;

    @Column(name = "COMPANY_NAME", nullable = false, length = 50)
    private String companyName;

    @Column(name = "BUSINESS_NUMBER", length = 20)
    private String businessNumber;

    @Column(name = "CEO_NAME", length = 50)
    private String ceoName;

    @Column(name = "COMPANY_TYPE")
    private CompanyCategory companyType;

    @Column(name = "TEL_NO", length = 20)
    private String telNo;

    @Column(name = "FAX_NO", length = 20)
    private String faxNo;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "ADDRESS", length = 255)
    private String address;

    @Column(name = "MANAGER_NAME", length = 50)
    private String managerName;

    @Column(name = "MANAGER_TEL", length = 20)
    private String managerTel;

    @Column(name = "STATUS")
    private CompanyStatus status;
}