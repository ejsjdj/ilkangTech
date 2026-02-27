package com.itwillbs.ilkwangtech.sales.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CompanyStatusConverter implements AttributeConverter<CompanyStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CompanyStatus attribute) {
        return (attribute == null) ? null : attribute.getCode();
    }

    @Override
    public CompanyStatus convertToEntityAttribute(Integer dbData) {
        return CompanyStatus.fromCode(dbData);
    }
}