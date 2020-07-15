package com.dna.rna.domain.Club;

import javax.persistence.AttributeConverter;

// 참고 https://woowabros.github.io/experience/2019/01/09/enum-converter.html
public class ClubeUserStatusConverter implements AttributeConverter<ClubUserStatus, Integer> {


    @Override
    public Integer convertToDatabaseColumn(ClubUserStatus attribute) {
        return attribute.getOrdinalStatus();
    }

    @Override
    public ClubUserStatus convertToEntityAttribute(Integer dbData) {
        return ClubUserStatus.ofOrdinalStatus(dbData);
    }

}
