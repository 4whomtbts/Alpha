package com.dna.rna.exception;

import com.dna.rna.DCloudResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(Exception ex, WebRequest req) {
        ApiErrorResponse error = new ApiErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {DCloudException.class})
    public ResponseEntity<DCloudResponse>  exception(DCloudException ex, WebRequest req) {
        DCloudResponse response = DCloudResponse.ofFailure(ex.getErrorMessage(), null, ex.getDebugMessage());
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

}
