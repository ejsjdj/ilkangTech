package com.itwillbs.ilkwangtech.production.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductionPlaneInsertDTO {

    private List<ProductionPlaneItemDTO> details;
    private Long routeCode;
    private String planeCode;
    private LocalDateTime planeDate;
    private Long member;
    private Long item;
    private Long totalQty;
    private String status;
    private String memo;
}
