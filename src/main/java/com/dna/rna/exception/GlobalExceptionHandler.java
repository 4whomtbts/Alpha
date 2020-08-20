package com.dna.rna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handle(Exception ex, WebRequest req) {
        ApiErrorResponse error = new ApiErrorResponse("DATA_ALREADY_EXISTS", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(Exception ex, WebRequest req) {
        ApiErrorResponse error = new ApiErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlphaException.class)
    public ResponseEntity<ApiErrorResponse> handleRnaException(AlphaException ex, WebRequest req) {

        ApiErrorResponse error = new ApiErrorResponse(ex.getErrorMessage(), ex.getDebugMessage());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
}
