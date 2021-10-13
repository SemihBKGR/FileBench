package com.semihbkgr.filebench.server.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

    private final ValidationResult validationResult;

    ValidationException(ValidationResult validationResult) {
        super(HttpStatus.BAD_REQUEST,validationResult.toString());
        this.validationResult = validationResult;
    }

}
