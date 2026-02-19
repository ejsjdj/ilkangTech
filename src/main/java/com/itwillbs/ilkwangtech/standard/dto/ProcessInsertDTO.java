package com.itwillbs.ilkwangtech.standard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessInsertDTO {

    private Long id; // 1. 라우트 기본 ID
    private String routeId; // 2. 라우트 ID(저장 시 자동 생성)
    private Long operationId; // 3. 공정 ID
    private Long itemId; // 4. 제품 ID(저장 시 자동 생성)
    private Long sequence; // 5. 라우트 순번
    private String routeName; // 6. 라우트명
    private String description; // 7. 라우트 설명
    private String note; // 8. 공정 비고
}
