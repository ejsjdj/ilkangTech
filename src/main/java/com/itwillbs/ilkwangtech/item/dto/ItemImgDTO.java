package com.itwillbs.ilkwangtech.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImgDTO {
    private Long id;
    private String imgName;
    private String originalImgName;
    private String imgUrl;
    private String repImgYn;
}
