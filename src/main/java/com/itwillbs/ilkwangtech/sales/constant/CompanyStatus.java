package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyStatus implements BaseEnum {

    ACTIVE(1, "거래중"),
    INACTIVE(0, "비활성화");

    private final int code;
    private final String description;

}