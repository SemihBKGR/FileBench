package com.semihbkgr.filebench.server.component;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NumericalIdGenerator implements IdGenerator<String> {

    @Value("${id.length:7}")
    public int idLength;

    @Override
    public String generate() {

        return RandomStringUtils.randomNumeric(idLength);
    }

}
