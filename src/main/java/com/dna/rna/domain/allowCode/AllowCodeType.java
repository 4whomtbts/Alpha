package com.dna.rna.domain.allowCode;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AllowCodeType {
    ACCOUNT_REGISTRATION(0),
    GROUP_JOIN(1);

    private int ordinalCode;

    AllowCodeType(int ordinalCode) {
        this.ordinalCode = ordinalCode;
    }

    public static AllowCodeType ofOrdinalStatus(int ordinalCode) {
        AllowCodeType result = Arrays.stream(AllowCodeType.values())
                .filter(v -> (v.getOrdinalCode() == ordinalCode))
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format("AllowCodeType 의 순서코드 [%d] 가 존재하지 않습니다.",
                                        ordinalCode)));
        System.out.println("결과 = " + result.getOrdinalCode());
        return result;
    }
}

