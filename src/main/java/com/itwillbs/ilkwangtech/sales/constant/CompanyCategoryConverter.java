package com.itwillbs.ilkwangtech.sales.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CompanyCategoryConverter implements AttributeConverter<CompanyCategory, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CompanyCategory attribute) {
        return (attribute == null) ? null : attribute.getCode();
    }

    @Override
    public CompanyCategory convertToEntityAttribute(Integer dbData) {
        return CompanyCategory.fromCode(dbData);
    }
}