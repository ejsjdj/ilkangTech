package com.itwillbs.ilkwangtech.standard.dto;

import lombok.*;

import java.util.List;


// 공정라우트 정보
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProcessRouteDTO {
    private String routeId; // 공정라우트 코드
    private Long itemId; // 제품 코드
    private String routeName; // 라우트명
    private String description; // 공정라우트 설명
    private String createdAt; // 생성일시
    private String constructor; // 생성자
    private List<String> item;

    @Builder
    public ProcessRouteDTO(String routeId, String description, Long itemId, String createdAt, String routeName, String constructor, List<String> item){
        super();
        this.routeId = routeId;
        this.itemId = itemId;
        this.routeName = routeName;
        this.description = description;
        this.createdAt = createdAt;
        this.constructor = constructor;
        this.item = item;

    }
}
