package com.semihbg.filebench.server.validation;

import com.semihbg.filebench.server.dto.BenchCreateDto;
import com.semihbg.filebench.server.model.Bench;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.regex.Pattern;

@Component
public class BenchValidator implements Validator<Bench> {

    private static final String SIMPLE_REGEX_PATTERN="[A-Za-z0-9]";

    private final Duration minExpiration;
    private final Duration maxExpiration;
    private final Pattern pattern;

    public BenchValidator(@Value("#{T(java.time.Duration).parse('${validate.bench.max-expiration}')}")Duration minExpiration,
                          @Value("#{T(java.time.Duration).parse('${validate.bench.min-expiration}')}")Duration maxExpiration) {
        this.minExpiration = minExpiration;
        this.maxExpiration = maxExpiration;
        this.pattern=Pattern.compile(SIMPLE_REGEX_PATTERN);
    }

    @Override
    public ValidationResult validate(BenchCreateDto benchCreateDto) {
        ValidationResult validationResult=ValidationResult.empty();
        if()
        return false;
    }


}
