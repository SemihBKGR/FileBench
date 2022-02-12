package com.semihbkgr.filebench.server.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.IntStream;

@Component
public class NumericalTokenGenerator implements TokenGenerator {

    private final int length;
    private final Random random;

    public NumericalTokenGenerator(@Value("${token.length}") int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be a positive value");
        this.length = length;
        this.random = new Random();
    }

    @Override
    public String token() {
        var token = new StringBuilder();
        IntStream.range(0, length)
                .forEach(i -> token.append((char) (random.nextInt(10) + 48)));
        return token.toString();
    }

}
