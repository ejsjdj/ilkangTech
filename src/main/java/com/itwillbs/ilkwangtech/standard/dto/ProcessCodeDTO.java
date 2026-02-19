package com.itwillbs.ilkwangtech.standard.dto;

import lombok.*;


// 공정라우트 정보
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProcessCodeDTO {
    private String routeId; // 공정라우트 코드
    private String description; // 공정라우트 설명
    private String itemId; // 제품 코드

    @Builder
    public ProcessCodeDTO(String routeId, String description, String itemId){
        super();
        this.routeId = routeId;
        this.description = description;
        this.itemId = itemId;
    }
}
