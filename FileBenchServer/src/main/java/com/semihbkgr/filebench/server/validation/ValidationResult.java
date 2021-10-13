package com.semihbkgr.filebench.server.validation;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        validationResult.addInvalidationUnit(invalidationUnit);
        return validationResult;
    }

    public static ValidationResult of(@NonNull InvalidationUnit... invalidationUnits) {
        ValidationResult validationResult = empty();
        for (InvalidationUnit invalidationUnit : invalidationUnits)
            validationResult.addInvalidationUnit(invalidationUnit);
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

    public void addInvalidationUnit(@NonNull ValidationResult.InvalidationUnit invalidationUnit) {
        this.invalidationUnitList.add(invalidationUnit);
    }

    public boolean isValid() {
        return invalidationUnitList.isEmpty();
    }

    public boolean isInvalid(){
        return !invalidationUnitList.isEmpty();
    }

    public Optional<ValidationException> createException(){
        if(isInvalid())
            return Optional.of(new ValidationException(this));
        return Optional.empty();
    }

    public void throwIfInvalid() {
        if (isInvalid())
            throw new ValidationException(this);
    }

    public void combine(@NonNull ValidationResult validationResult) {
        this.invalidationUnitList.addAll(validationResult.invalidationUnitList);
    }

    public String getMessage(){
        var messageSB=new StringBuilder();
        var iterator=invalidationUnitList.iterator();
        while(iterator.hasNext()){
            var invalidationUnit=iterator.next();
            messageSB.append(invalidationUnit.toString());
            if(iterator.hasNext())
                messageSB.append(System.lineSeparator());
        }
        return messageSB.toString();
    }

    @Data
    @Builder
    public static class InvalidationUnit {
        private final Class<?> type;
        private final String field;
        private final String message;

        @Override
        public String toString() {
            return String.format("%s <%s> : %s",field,type.getSimpleName(),message);
        }

    }

}
