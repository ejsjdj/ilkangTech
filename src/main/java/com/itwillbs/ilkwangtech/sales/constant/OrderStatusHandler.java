package com.itwillbs.ilkwangtech.sales.constant;

import org.apache.ibatis.type.MappedTypes;

@MappedTypes(OrderStatus.class)
public class OrderStatusHandler extends CommonEnumTypeHandler<OrderStatus> {
    public OrderStatusHandler() {
        super(OrderStatus.class);
    }
}
