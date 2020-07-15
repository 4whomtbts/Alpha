package com.dna.rna.domain.ClubUser;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ClubUserStatus {

    ACTIVE("활동 중", 0),
    DORMANT("휴면 회원", 5),
    EXIT("탈퇴 회원", 10),
    EXILED("제명 회원", 15);

    private String stringStatus;
    private int ordinalStatus;

    ClubUserStatus(String stringStatus, int ordinalStatus) {
        this.stringStatus = stringStatus;
        this.ordinalStatus = ordinalStatus;
    }

    public static ClubUserStatus ofOrdinalStatus(int ordinalStatus) {
        return Arrays.stream(ClubUserStatus.values())
                .filter(v -> (v.getOrdinalStatus() == ordinalStatus))
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format("ClubUserStatus 의 순서코드 [%d] 가 존재하지 않습니다.",
                                        ordinalStatus)));
    }
}
