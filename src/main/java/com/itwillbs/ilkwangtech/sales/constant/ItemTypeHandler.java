package com.itwillbs.ilkwangtech.sales.constant;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ItemType.class)
public class ItemTypeHandler extends CommonEnumTypeHandler<ItemType> {
    public ItemTypeHandler() {
        super(ItemType.class);
    }
}
