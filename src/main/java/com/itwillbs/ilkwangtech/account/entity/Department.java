package com.itwillbs.ilkwangtech.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "departments")
@ToString
public class Department {
    // 부서 (0 임원 1 인사 2 구매 3 영업 4 재무회계 5 정보시스템 6 경영 7 안전 8 법무 100 공장장 101 프레스 102 사출 103 도장 104 조립 105 품질 106 금형 107 생산관리 108)
    // DB 에 저장 공간을 절약하고 부서를 효율적으로 관리하기 위한 Entity이다.
    // 부서를 추가하고 삭제하는 기능 구현에 필요
    // 부서의 상태를 설정하는 기능구현에 필요
    @Id
    Integer id;
    String departmentName;
    String parentDepartment;
    boolean isActive;

}
