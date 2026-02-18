package com.itwillbs.ilkwangtech.standard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// 공정라우트 정보
@Getter
@Setter
@NoArgsConstructor
public class ProcessCodeDTO {

    private String route_id; // 공정라우트 코드
    private String description; // 공정라우트 설명
    private String item_id; // 제품 코드


}
