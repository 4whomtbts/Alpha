package com.dna.rna.domain.allowCode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AllowCodeTypeConverter implements AttributeConverter<AllowCodeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AllowCodeType attribute) {
        return attribute.getOrdinalCode();
    }

    @Override
    public AllowCodeType convertToEntityAttribute(Integer dbData) {
        return AllowCodeType.ofOrdinalStatus(dbData);
    }
}
