package com.semihbg.filebench.server.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NumericalIdGenerator implements IdGenerator<String> {

    //For only generate numerical characters
    private static final int LEFT_LIMIT=48;
    private static final int RIGHT_LIMIT=58;

    private final int idLength;
    private final Random random;

    public NumericalIdGenerator(@Value("${id.bench.length:7}") int idLength) {
        this.idLength = idLength;
        random=new Random();
    }

    @Override
    public String generate() {
        return random.ints(LEFT_LIMIT,RIGHT_LIMIT)
                .limit(idLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
