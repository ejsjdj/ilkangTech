package com.itwillbs.ilkwangtech.sales.constant;

import org.apache.ibatis.type.MappedTypes;

@MappedTypes(OrderDetailStatus.class)
public class OrderDetailStatusHandler extends CommonEnumTypeHandler<OrderDetailStatus> {
    public OrderDetailStatusHandler() {
        super(OrderDetailStatus.class);
    }
}
