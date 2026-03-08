package com.itwillbs.ilkwangtech.item.dto;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemDTO {

    private Long itemId;

    private String itemCode;

    private String itemName;

    private ItemType itemType;

    private String uom;

    private Long standardPrice;

    private List<ItemImgDTO> itemImgList = new ArrayList<>();
}