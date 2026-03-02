package com.itwillbs.ilkwangtech.inventorymg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor // 추가
@AllArgsConstructor// 추가
public class RackItemDTO {
	private Long inventoryId;  // 재고 고유 식별자
    private String itemCode;   // 품목 코드
    private String itemName;   // 품목명
    private String lotNumber;  // LOT 번호
    private Long quantity;     // 수량
    private LocalDate expirationDate; // 유통기한

}