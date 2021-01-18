package com.dna.rna.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * api 에 response 하는 error 의 template
 *
 * ApiErrorResponse.java
 * created 2020.4.2
 * @author Hyounjun kim <4whomtbts@gmail.com>
 *
 */

@Getter
@Setter
public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String error;
    private String message;

    public ApiErrorResponse(String error, String message) {
        this.timestamp = LocalDateTime.now();
        this.error = error;
        this.message = message;
    }
}
