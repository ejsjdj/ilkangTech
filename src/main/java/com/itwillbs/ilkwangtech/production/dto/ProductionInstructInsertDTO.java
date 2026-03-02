package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.production.entity.ProductionInstructEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneDetailEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionWorkerEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProductionInstructInsertDTO {


    // 작업지시 코드
    private String instructCode;

    // 생산계획 ID
    private Long productionId;

    // 품목 ID
    private Long item;

    // 지시 수량
    private Long instructQty;

    // 불량
    private Long defective;

    // 상태
    private String status;

    // 지시등록일
    private LocalDateTime startDate;

    //==================================//

    // 생산완료일
    private LocalDateTime endDate;


    // 작업자
    private List<ProductionInstructWorkerInsertDTO> workers;
}
