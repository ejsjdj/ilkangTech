package com.itwillbs.ilkwangtech.sales.constant;

import org.apache.ibatis.type.MappedTypes;

@MappedTypes(CompanyStatus.class)
public class CompanyStatusHandler extends CommonEnumTypeHandler<CompanyStatus> {
    public CompanyStatusHandler() {
        super(CompanyStatus.class);
    }
}
