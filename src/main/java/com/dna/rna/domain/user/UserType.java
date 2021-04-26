package com.dna.rna.domain.user;

import lombok.Getter;

/**
 * 유저의 role을 구분하기 위한 enumeration
 *
 * UserType.java
 * created 2020.4.1
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
public enum UserType {

    USER("USER"),
    ADMIN("ADMIN");

    private String userTypeValue;

    UserType(String userTypeValue) {
        this.userTypeValue = userTypeValue;
    }


}
