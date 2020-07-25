package com.dna.rna.domain.clubUser;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// 참고 https://woowabros.github.io/experience/2019/01/09/enum-converter.html
@Converter(autoApply = true)
public class ClubUserStatusConverter implements AttributeConverter<ClubUserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ClubUserStatus attribute) {
        return attribute.getStatusCode();
    }

    @Override
    public ClubUserStatus convertToEntityAttribute(Integer dbData) {
        return ClubUserStatus.ofOrdinalStatus(dbData);
    }
}
