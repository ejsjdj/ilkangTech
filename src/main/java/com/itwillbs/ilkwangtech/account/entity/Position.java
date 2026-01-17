package com.itwillbs.ilkwangtech.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "positions")
@ToString
public class Position {
    // 직급 (1 부장 2 차장 3 과장 4 대리 5 주임 6 사원 51 이사 52 상무이사 53 전무이사 54 부사장 55 사장 56 대표이사)
    //     (101 기능공 102 기능사 103 선임기능사 104 기능장 105 수석기능장)

    @Id
    private int id;

    private String positionName; // "부장", "대리", "사원"
    private String positionType;
    boolean isActive;

}
