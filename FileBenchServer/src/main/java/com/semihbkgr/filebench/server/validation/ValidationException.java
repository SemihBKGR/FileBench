package com.semihbkgr.filebench.server.validation;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

    private final ValidationResult validationResult;

    public ValidationException(@NonNull ValidationResult validationResult) {
        super(HttpStatus.BAD_REQUEST);
        this.validationResult = validationResult;
    }

    @Override
    public String getMessage() {
        return validationResult.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

}
