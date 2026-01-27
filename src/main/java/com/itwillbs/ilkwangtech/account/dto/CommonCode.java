package com.itwillbs.ilkwangtech.account.dto;

import lombok.*;

/**
 * 권한 목록 조회 시 사용하는 공통코드 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommonCode {
    private Long id;                // 코드 고유 ID
    private String commonCodeName;  // 코드명 (예: 전체 관리자 권한)
}
