package com.semihbkgr.filebench.server.component;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDTokenGenerator implements TokenGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
