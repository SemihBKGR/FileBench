package com.semihbkgr.filebench.server.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private final List<InvalidationUnit> invalidationUnitList;

    private ValidationResult() {
        this.invalidationUnitList = new ArrayList<>();
    }

    public static ValidationResult empty() {
        return new ValidationResult();
    }

    public static ValidationResult of(@NonNull ValidationResult.InvalidationUnit invalidationUnit) {
        ValidationResult validationResult = empty();
        validationResult.addInvalidation(invalidationUnit);
        return validationResult;
    }

    public static ValidationResult of(@NonNull InvalidationUnit... invalidationUnits) {
        ValidationResult validationResult = empty();
        for (InvalidationUnit invalidationUnit : invalidationUnits)
            validationResult.addInvalidation(invalidationUnit);
        return validationResult;
    }

    public static ValidationResult combine(ValidationResult oneValidationResult, ValidationResult twoValidationResult) {
        ValidationResult validationResult = empty();
        validationResult.combine(oneValidationResult);
        validationResult.combine(twoValidationResult);
        return validationResult;
    }

    public static ValidationResult combine(ValidationResult... validationResults) {
        ValidationResult validationResult = empty();
        for (ValidationResult vr : validationResults)
            validationResult.combine(vr);
        return validationResult;
    }

    public void addInvalidation(@NonNull ValidationResult.InvalidationUnit invalidationUnit) {
        this.invalidationUnitList.add(invalidationUnit);
    }

    public boolean isValid() {
        return invalidationUnitList.isEmpty();
    }

    public boolean isInvalid(){
        return !invalidationUnitList.isEmpty();
    }

    public void throwIfInvalid() {
        if (isInvalid())
            throw new ValidationException(this);
    }

    public void throwIfInvalid(String message) {
        if (isInvalid())
            throw new ValidationException(message, this);
    }

    public void combine(@NonNull ValidationResult validationResult) {
        this.invalidationUnitList.addAll(validationResult.invalidationUnitList);
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class InvalidationUnit {
        private final Class<?> type;
        private final String field;
        private final String message;
    }

}
