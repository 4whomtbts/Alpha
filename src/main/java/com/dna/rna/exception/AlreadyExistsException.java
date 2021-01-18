package com.dna.rna.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String fieldName, String value) {
        super("{ "+ value +" } " + "of  field name [ " + fieldName + " ] already exists");
    }
}
