package com.itwillbs.ilkwangtech.sales.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnitStatus {

    KG(10, "킬로그램", UnitCategory.WEIGHT),
    G(20, "그램", UnitCategory.WEIGHT),
    L(30, "리터", UnitCategory.VOLUME),
    ML(40, "밀리리터", UnitCategory.VOLUME),
    CM(50, "센티미터", UnitCategory.LENGTH),
    M(90, "미터", UnitCategory.LENGTH);

    private final int code;
    private final String label;
    private final UnitCategory category;
}