package com.semihbkgr.filebench.server.error;

import com.semihbkgr.filebench.server.validation.ValidationResult;

public class ValidationException extends IllegalArgumentException{

    private final ValidationResult validationResult;

    public ValidationException(ValidationResult validationResult) {
        this.validationResult=validationResult;
    }

    public ValidationException(String s, ValidationResult validationResult) {
        super(s);
        this.validationResult = validationResult;
    }

}
