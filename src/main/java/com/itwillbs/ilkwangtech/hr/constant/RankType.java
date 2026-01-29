package com.itwillbs.ilkwangtech.hr.constant;

import lombok.Getter;

@Getter
public enum RankType {
    // 일반 직급 (1 ~ 6)
    MANAGER_DEPT(1, "부장"),
    DEPUTY_MANAGER(2, "차장"),
    MANAGER(3, "과장"),
    ASSISTANT_MANAGER(4, "대리"),
    SENIOR_STAFF(5, "주임"),
    STAFF(6, "사원"),

    // 임원 직급 (51 ~ 56)
    DIRECTOR(51, "이사"),
    MANAGING_DIRECTOR(52, "상무이사"),
    SENIOR_MANAGING_DIRECTOR(53, "전무이사"),
    VICE_PRESIDENT(54, "부사장"),
    PRESIDENT(55, "사장"),
    CEO(56, "대표이사");

    private final int code;
    private final String name;

    RankType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 직급 코드를 넣으면 해당 Enum 객체를 반환합니다.
     */
    public static int fromCode(String name) {
        for (RankType rank : RankType.values()) {
            if (rank.getName().equals(name)) {
                return rank.getCode();
            }
        }
        // 일치하는 명칭이 없을 경우 기본값 또는 예외 처리
        throw new IllegalArgumentException("존재하지 않는 직급 명칭입니다: " + name);
    }
}
