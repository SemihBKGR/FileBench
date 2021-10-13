package com.semihbkgr.filebench.server.validation;

import reactor.core.publisher.Mono;

public interface Validator<T> {

    Mono<ValidationResult> validate(T t);

}
