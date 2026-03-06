package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompanyDTO {

//    CREATE TABLE Company (
//    company_id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
//    company_code VARCHAR2(50) NOT NULL UNIQUE,
//    company_name VARCHAR2(50) NOT NULL,
//    company_type INTEGER,
//    CEO_name VARCHAR2(50),
//    TEL_NO VARCHAR2(20)
//    );

    private Long companyId;
    private String companyCode;
    private String companyName;
    private String email;
    private String companyType;
    private CompanyCategory category;
    private String ceoName;
    private String telNo;
}