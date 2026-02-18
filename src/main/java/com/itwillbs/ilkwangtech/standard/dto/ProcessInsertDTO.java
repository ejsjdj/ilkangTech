package com.itwillbs.ilkwangtech.standard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessInsertDTO {

    private String id; // 1. 라우트 기본 ID
    private String routeId; // 2. 라우트 ID
    private String sequence; // 3. 라우트 순번
    private String operationId; // 4. 공정 ID
    private String note; // 5. 비고
    // private String name // 6. 생성자 이름
    // private String createDate // 7. 생성일시


}
