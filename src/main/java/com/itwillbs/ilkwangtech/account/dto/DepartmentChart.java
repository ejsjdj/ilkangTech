package com.itwillbs.ilkwangtech.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DepartmentChart {

    Integer id;
    String departmentName;
    Long parentDepartment;
    boolean isActive;

}