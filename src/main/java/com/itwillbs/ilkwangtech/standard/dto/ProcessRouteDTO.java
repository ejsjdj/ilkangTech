package com.itwillbs.ilkwangtech.standard.dto;

import lombok.*;

import java.util.List;


// 공정라우트 정보
@Getter
@Setter
@NoArgsConstructor
public class ProcessRouteDTO {
    private Long itemId;
    private String routeCode; // 공정라우트 코드
    private String itemName; // 품목명
    private String routeName; // 라우트명
    private String description; // 공정라우트 설명
    private String createdAt; // 생성일시
    private String memberName; // 생성자

    @Builder
    public ProcessRouteDTO(String routeCode, String description, Long itemId, String itemName, String createdAt, String routeName, String constructor){
        super();
        this.routeCode = routeCode;
        this.itemId = itemId;
        this.itemName = itemName;
        this.routeName = routeName;
        this.description = description;
        this.createdAt = createdAt;
        this.memberName = constructor;
    }
}
