package com.itwillbs.ilkwangtech.process.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProcessStepDetailDTO {
    private String operationName;    // 공정명 (description)
    private String operationId;      // 공정코드
    private String memberId;         // 담당자 ID (member_id)
    private String memberName;       // 담당자 성함 (members.name)
    private Integer operationQty;    // 수량
    private Integer defectiveQty;    // 불량수량
    private String equipName;        // 설비명
    private String equipCode;        // 설비코드
}
