package com.itwillbs.ilkwangtech.account.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MemberStatusConverter implements AttributeConverter<MemberStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MemberStatus status) {
        return (status == null) ? null : status.getCode();
    }

    @Override
    public MemberStatus convertToEntityAttribute(Integer dbData) {
        return MemberStatus.fromCode(dbData);
    }
}
