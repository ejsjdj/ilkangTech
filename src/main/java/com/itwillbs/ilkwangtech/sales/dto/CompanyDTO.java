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
    private Long CompanyId;
    private String name;
    private String email;
    private String phone;
    private String manager;
    private CompanyCategory category;
    private CompanyStatus valid;

}