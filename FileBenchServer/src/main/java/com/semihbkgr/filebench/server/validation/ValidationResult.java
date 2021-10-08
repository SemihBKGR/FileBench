package com.semihbkgr.filebench.server.validation;

import com.semihbkgr.filebench.server.error.ValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private final List<InvalidMessage> invalidMessageList;

    private ValidationResult() {
        this.invalidMessageList = new ArrayList<>();
    }

    public static ValidationResult empty() {
        return new ValidationResult();
    }

    public static ValidationResult of(@NonNull InvalidMessage invalidMessage) {
        ValidationResult validationResult = empty();
        validationResult.addInvalidation(invalidMessage);
        return validationResult;
    }

    public static ValidationResult of(@NonNull InvalidMessage... invalidMessages) {
        ValidationResult validationResult = empty();
        for (InvalidMessage invalidMessage : invalidMessages)
            validationResult.addInvalidation(invalidMessage);
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

    public void addInvalidation(@NonNull InvalidMessage invalidMessage) {
        this.invalidMessageList.add(invalidMessage);
    }

    public boolean isValid() {
        return invalidMessageList.size() == 0;
    }

    public void throwIfInvalid() {
        if (!isValid())
            throw new ValidationException(this);
    }

    public void throwIfInvalid(String message) {
        if (!isValid())
            throw new ValidationException(message, this);
    }

    public void combine(@NonNull ValidationResult validationResult) {
        this.invalidMessageList.addAll(validationResult.invalidMessageList);
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class InvalidMessage {
        private final Class<?> type;
        private final String field;
        private final String message;
    }

}
