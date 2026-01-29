package com.itwillbs.ilkwangtech.Hr.constant;

import lombok.Getter;

@Getter
public class DepartmentType {
    public enum Department {
        // 임원 및 사무직 (0 ~ 100 미만)
        EXECUTIVE(0, "임원"),
        HR(1, "인사"),
        PURCHASING(2, "구매"),
        SALES(3, "영업"),
        FINANCE(4, "재무회계"),
        IT(5, "정보시스템"),
        MANAGEMENT(6, "경영"),
        SAFETY(7, "안전"),
        LEGAL(8, "법무"),

        // 현장 및 생산직 (100 이상)
        PLANT_MANAGER(100, "공장장"),
        PRESS(101, "프레스"),
        INJECTION(102, "사출"),
        PAINTING(103, "도장"),
        ASSEMBLY(104, "조립"),
        QUALITY(105, "품질"),
        MOLD(106, "금형"),
        PRODUCTION_MGMT(107, "생산관리"),
        ETC(108, "기타"); // 명칭이 없는 108번은 임의로 기타 처리

        private final int code;
        private final String name;

        Department(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() { return code; }
        public String getName() { return name; }

        /**
         * 부서 코드를 넣으면 해당 Enum 객체를 반환합니다.
         * @param code 부서 숫자 코드 (예: 1)
         * @return Department Enum (예: HR)
         */
        public static Department fromCode(String code) {
            if (code == null || code.isEmpty()) return null; // 방어 코드 추가

            try {
                int numericCode = Integer.parseInt(code); // 문자열 "1" -> 숫자 1
                for (Department dept : Department.values()) {
                    if (dept.code == numericCode) {
                        return dept;
                    }
                }
            } catch (NumberFormatException e) {
                // 숫자가 아닌 문자열이 들어왔을 경우 예외 처리
            }
            throw new IllegalArgumentException("일치하는 부서 코드가 없습니다: " + code);
        }
    }
}
