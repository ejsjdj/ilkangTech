package com.itwillbs.ilkwangtech.sales.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderDetailStatusConverter implements AttributeConverter<OrderDetailStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderDetailStatus attribute) {
        return (attribute == null) ? null : attribute.getCode();
    }

    @Override
    public OrderDetailStatus convertToEntityAttribute(Integer dbData) {
        return OrderDetailStatus.fromCode(dbData);
    }
}