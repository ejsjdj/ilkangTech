package com.itwillbs.ilkwangtech.standard.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomTreeDTO {
    private Long itemId;
    private String itemCode;
    private String itemName;
    private String itemType;
    private Long parentItemId;
    private Long bomId;
    private Double requireQty;
    private Integer level;
    private String path;
    private String imgUrl;

    @Builder.Default
    private List<BomTreeDTO> children = new ArrayList<>();
    
    public void addChild(BomTreeDTO child) {
        this.children.add(child);
    }
}
