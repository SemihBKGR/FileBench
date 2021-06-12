package com.semihbg.filebench.server.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FileIdGenerator implements IdGenerator<String>{

    //For only letters and numerical characters
    private static final int LEFT_LIMIT=48;
    private static final int RIGHT_LIMIT=122;

    private final int idLength;
    private final Random random;

    public FileIdGenerator(@Value("${id.file.length:25}")int idLength) {
        this.idLength=idLength;
        this.random=new Random();
    }

    @Override
    public String generate() {
        return random.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(idLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
