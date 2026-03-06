package com.itwillbs.ilkwangtech.standard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessRouteInsertDTO {

    private String routeCode; // 1. 라우트 코드(저장 시 자동 생성)
    private Long operationId; // 2. 공정 ID
    private Long itemId; // 3. 제품 ID(저장 시 자동 생성)
    private Long sequence; // 4. 라우트 순번
    private String routeName; // 5. 라우트명
    private String description; // 6. 라우트 설명
    private String note; // 7. 공정 비고
}
