package com.dna.rna.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 서버 전반에서 사용되는 Exception wrapper 로써
// debug flag를 두어서 debug 시에는 nested exception 과
// detail error message를 반환하고
// production 시에는 INTERNAL_SERVER_ERROR 혹은 ILLEGAL_ARGUMENT 와
// 간단한 이유만 알려주어서 feedback을 줄 수 있도록 한다.
@Getter
public class DCloudException extends RuntimeException {

    private static final String ILLEGAL_ARGUMENT = "ILLEGAL_ARGUMENT";
    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    private String errorMessage;
    private String debugMessage;
    private HttpStatus statusCode;

    private DCloudException(String errorMessage, String debugMessage, HttpStatus statusCode) {
        super("상태코드 [" + statusCode + "] | " + "에러 = " + errorMessage + "\n 디버깅메시지 = " + debugMessage);
        this.errorMessage = errorMessage;
        this.debugMessage = debugMessage;
        this.statusCode = statusCode;
    }

    public static DCloudException ofIllegalArgumentException(String errorMessage) {
        return DCloudException.ofIllegalArgumentException(errorMessage, null);
    }

    public static DCloudException ofIllegalArgumentException(String errorMessage, String debugMessage) {
        if (debugMessage == null) debugMessage = "";
        return new DCloudException(errorMessage, debugMessage, HttpStatus.BAD_REQUEST);
    }

    public static DCloudException ofInternalServerError(String debugMessage) {
        return DCloudException.ofInternalServerError("서버에 오류가 발생했습니다.", debugMessage);
    }

    public static DCloudException ofInternalServerError(String errorMessage, String debugMessage) {
        if (debugMessage == null) debugMessage = "";
        return new DCloudException(errorMessage, debugMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
