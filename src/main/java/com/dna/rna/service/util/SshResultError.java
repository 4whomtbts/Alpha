package com.dna.rna.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SshResultError {

    private String error;
    private int code;

    @Override
    public String toString() {
        return String.format("[SshResultError] error : %s, code : %s", error, code);
    }
}
