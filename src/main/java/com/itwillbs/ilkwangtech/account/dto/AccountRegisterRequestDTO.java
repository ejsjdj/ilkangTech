package com.itwillbs.ilkwangtech.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccountRegisterRequestDTO {

    private static final long serialVersionUID = 1L;

    String name;            	// 이름
    Integer gender;          	// 성별
    String employeeNumber;  	// 사원번호
    LocalDate hireDate;     // 입사일
    String residentNumber;  	// 주민등록번호
    String email;           	// 이메일
    String phoneNumber;     	// 전화번호
    String password;        // 비밀번호
    Integer department;      	// 부서
    Integer position;        	// 직급
    Integer bank;            	// 은행
    String accountNumber;   	// 계좌번호

    public void setEmployeeNumber(int idx) {
        int year = this.hireDate.getYear() % 100;
        int number = idx;
        if (number / 10000 == 0) number = number + 10000;
        this.employeeNumber = year + "-" + number;
    }
}
