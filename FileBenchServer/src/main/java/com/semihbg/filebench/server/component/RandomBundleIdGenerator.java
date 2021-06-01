package com.smh.bs.server.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomBundleIdGenerator implements BundleIdGenerator {

    private static final int LEFT_LIMIT=48;
    private static final int RIGHT_LIMIT=122;

    private final int idLength;
    private final Random random;

    @Autowired
    public RandomBundleIdGenerator(@Value("${bundle.id.length:7}") int idLength) {
        this.idLength = idLength;
        random=new Random();
    }

    @Override
    public String generate() {
        return randomString(idLength);
    }

    private String randomString(int length){
        return random.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
