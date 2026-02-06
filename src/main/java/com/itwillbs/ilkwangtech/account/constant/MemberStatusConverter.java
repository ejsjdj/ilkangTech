package com.itwillbs.ilkwangtech.account.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)    // 엔티티에 자동적용
public class MemberStatusConverter implements AttributeConverter<MemberStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MemberStatus attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public MemberStatus convertToEntityAttribute(Integer dbData) {
        return (dbData == null) ? null : MemberStatus.fromCode(dbData);
    }

}
