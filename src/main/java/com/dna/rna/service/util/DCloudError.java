package com.dna.rna.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DCloudError {
    private String error;
    private int code;

    @Override
    public String toString() {
        return String.format("[DcloudError] => error : [%s], code : [%s]", error, code);
    }
}
