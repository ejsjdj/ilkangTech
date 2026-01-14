package com.itwillbs.ilkwangtech.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 회원가입 요청을 처리한 후
// 성공이나 실패 상태를 응답받기위해 필요한 DTO
// 성공시 성공여부 true, 응답메시지 가입성공, 가입된 아이디 출력, 회원정보값 저장후 전달
// 실패시 가입성공여부 false, 응답메시지 ~ 때문에 안된다 메시지, accountId = null, account = null
public class AccountRegisterResponseDTO {
	
	private boolean success;	// 가입 성공 여부
	private String message;		// 응답 메시지
	private Long employeeNumber;// 생성된 사원 번호
	
	private String duplicateField;
	private String duplicateValue;
	
	// 성공 응답 생성
	// 성공시 해당 메서드를 호출해서 회원가입 성공을 처리
    public static AccountRegisterResponseDTO success(Long employeeNumber) {
    	return AccountRegisterResponseDTO.builder()
    			.success(true)
    			.message("회원가입이 완료되었습니다")
    			.employeeNumber(employeeNumber)
    			.build();
    }
    
    // 실패 응답 생성
    // 실패시 해당 메서드를 호출해서 회원가입 실패를 처리
    public static AccountRegisterResponseDTO duplicateError(String fieldName, String fieldValue) {
    	return AccountRegisterResponseDTO.builder()
    			.message(fieldName + " : " + fieldValue + "은(는) 이미 사용중입니다")
    			.duplicateField(fieldName)
    			.duplicateValue(fieldValue)
    			.build();
    }
}
