package com.itwillbs.ilkwangtech.sales.entity;

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
    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "COMPANY_CODE", nullable = false, length = 50)
    private String companyCode;

    @Column(name = "COMPANY_NAME", nullable = false, length = 50)
    private String companyName;

    @Column(name = "COMPANY_TYPE")
    private Long companyType;

    @Column(name = "CEO_NAME", length = 50)
    private String ceoName;

    @Column(name = "TEL_NO", length = 20)
    private String telNo;
}