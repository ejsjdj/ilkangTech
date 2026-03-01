package com.itwillbs.ilkwangtech.standard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessRouteDetailDTO {

    private String id; // 라우트 단계 id
    private String routeCode; // 라우트 id
    private String sequence; // 라우트 순서
    private String note; // 비고


}
