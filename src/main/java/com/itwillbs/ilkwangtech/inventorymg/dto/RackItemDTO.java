package com.itwillbs.ilkwangtech.inventorymg.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RackItemDTO {
	private Long inventoryId; // 추가됨 (재고 고유 식별자)
	private String itemCode;
	private String itemName;
	private String lotNumber;
	private Long quantity;
	private LocalDate expirationDate;
}