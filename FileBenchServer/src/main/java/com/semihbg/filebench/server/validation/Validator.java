package com.semihbg.filebench.server.validation;

public interface Validator<T> {

    ValidationResult validate(T t);

}
