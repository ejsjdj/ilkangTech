package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChartDataDTO {
    private List<String> categories; // X축 라벨 (예: 2025-10, 02-27 등)
    private List<Long> inData;       // 입고 데이터
    private List<Long> outData;      // 출고 데이터
    private List<Long> discardData;  // 폐기 데이터
}