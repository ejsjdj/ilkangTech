package com.itwillbs.ilkwangtech.production.dto;

import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneDetailEntity;
import com.itwillbs.ilkwangtech.production.entity.ProductionPlaneEntity;
import com.itwillbs.ilkwangtech.standard.entity.ProcessEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
public class ProductionInstructInsertDTO {

    private String instructCode;

    private Long lotId;

    private ProductionPlaneEntity productionId;

    private ProductionPlaneDetailEntity detailId;

    private ProcessEntity process;

    private Long instructQty;

    private Long member;

    private String status;

}
