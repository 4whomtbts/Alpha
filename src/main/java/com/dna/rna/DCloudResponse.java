package com.dna.rna;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DCloudResponse {

    private String result;
    private String error;
    private String message;
    private String debugMessage;
    private LocalDateTime timestamp;

    private DCloudResponse(String result, String error, String message, String debugMessage) {
        this.result = result;
        this.error = error;
        this.message = message;
        String profiles = System.getProperty("spring.profiles.active");
        this.debugMessage = profiles.contains("prod")? null : debugMessage;
        this.timestamp = LocalDateTime.now();
    }

    public static DCloudResponse ofSuccess(String result) {
        return new DCloudResponse(result, null, null, null);
    }

    public static DCloudResponse ofFailure(String error, String message, String debugMessage) {
        return new DCloudResponse(null, error, message, debugMessage);
    }
}
