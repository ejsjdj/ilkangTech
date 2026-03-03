package com.itwillbs.ilkwangtech.process.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LotResponseDTO {
    private String lotId;      
    private String itemName;   // ITEM 테이블에서 가져온 이름
    private String lotType;
    private String status;     
    private LocalDateTime createdDate; 
}
