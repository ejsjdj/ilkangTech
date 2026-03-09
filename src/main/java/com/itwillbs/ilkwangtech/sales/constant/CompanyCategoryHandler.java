package com.itwillbs.ilkwangtech.sales.constant;

import org.apache.ibatis.type.MappedTypes;

@MappedTypes(CompanyCategory.class)
public class CompanyCategoryHandler extends CommonEnumTypeHandler<CompanyCategory> {
    public CompanyCategoryHandler() {
        super(CompanyCategory.class);
    }
}
