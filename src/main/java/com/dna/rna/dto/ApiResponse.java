package com.dna.rna.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ApiResponse {

    private String result;

    public ApiResponse() {}

    public static ApiResponse OK() {
        return new ApiResponse("OK");
    }
    public ApiResponse(String result) {
        this.result = result;
    }
}
