package com.dna.rna.domain.groupUser;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GroupUserType {
    REQUESTED_JOIN(-1),
    MANAGER(0),
    MEMBER(1);

    private int ordinalCode;

    GroupUserType(int ordinalCode) {
        this.ordinalCode = ordinalCode;
    }

    public static GroupUserType ofOrdinalStatus(int ordinalCode) {
        GroupUserType result = Arrays.stream(GroupUserType.values())
                .filter(v -> (v.getOrdinalCode() == ordinalCode))
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format("GroupUserType 의 순서코드 [%d] 가 존재하지 않습니다.",
                                        ordinalCode)));
        System.out.println("결과 = " + result.getOrdinalCode());
        return result;
    }
}


