package com.dna.rna.domain.groupUser;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class GroupUserTypeConverter implements AttributeConverter<GroupUserType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(GroupUserType attribute) {
        return attribute.getOrdinalCode();
    }

    @Override
    public GroupUserType convertToEntityAttribute(Integer dbData) {
        return GroupUserType.ofOrdinalStatus(dbData);
    }
}
