package com.itwillbs.ilkwangtech.standard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessUpdateDTO {

    private Long id; // 라우트 ID
    private Long operationId; // 공정 ID
    private Long sequence; // 순서
    private String description; // 설명
    private String note; // 비고

}
