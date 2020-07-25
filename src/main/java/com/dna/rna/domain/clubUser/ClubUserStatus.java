package com.dna.rna.domain.clubUser;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ClubUserStatus {

    /*
    ACTIVE("활동 중", "0"),
    DORMANT("휴면 회원", "5"),
    EXIT("탈퇴 회원", "10"),
    EXILED("제명 회원", "15");
    */
    ACTIVE("활동 중", 0),
    DORMANT("휴면", 10),
    EXIT("탈퇴", 20),
    EXILED("제명", 30);

    private String stringStatus;
    private int statusCode;

    ClubUserStatus(String stringStatus, int statusCode) {
        this.stringStatus = stringStatus;
        this.statusCode = statusCode;
    }

    public static ClubUserStatus ofOrdinalStatus(int ordinalStatus) {
        ClubUserStatus result = Arrays.stream(ClubUserStatus.values())
                .filter(v -> (v.getStatusCode() == ordinalStatus))
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                String.format("ClubUserStatus 의 순서코드 [%d] 가 존재하지 않습니다.",
                                        ordinalStatus)));
        System.out.println("결과 = " + result.stringStatus);
        return result;
    }

    public String getStringStatus() {
        return stringStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
