package com.itwillbs.ilkwangtech.item.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ItemTypeConverter implements AttributeConverter<ItemType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ItemType attribute) {
        return (attribute == null) ? null : attribute.getCode();
    }

    @Override
    public ItemType convertToEntityAttribute(Integer dbData) {
        return ItemType.fromCode(dbData);
    }
}