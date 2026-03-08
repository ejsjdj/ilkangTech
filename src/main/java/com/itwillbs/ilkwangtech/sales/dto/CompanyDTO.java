package com.itwillbs.ilkwangtech.sales.dto;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.constant.CompanyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompanyDTO {
    private Long companyId;
    private String companyCode;
    private String companyName;
    private String businessNumber;
    private String ceoName;
    private CompanyCategory companyType;
    private String telNo;
    private String faxNo;
    private String email;
    private String address;
    private String managerName;
    private String managerTel;
    private CompanyStatus status;

}