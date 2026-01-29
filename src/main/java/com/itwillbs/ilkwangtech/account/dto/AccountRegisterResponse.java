package com.itwillbs.ilkwangtech.account.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
// 회원가입 요청을 처리한 후
// 성공이나 실패 상태를 응답받기위해 필요한 DTO
// 성공시 성공여부 true, 응답메시지 가입성공, 가입된 아이디 출력, 회원정보값 저장후 전달
// 실패시 가입성공여부 false, 응답메시지 ~ 때문에 안된다 메시지, accountId = null, account = null
public class AccountRegisterResponse {

    private Long id;            // 생성된 사원의 id
    private boolean success;	// 가입 성공 여부
    private String message;		// 응답 메시지
    private String employeeNumber; // 생성된 사원 정보

}