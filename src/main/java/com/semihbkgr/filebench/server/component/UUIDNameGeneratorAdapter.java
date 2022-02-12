package com.semihbkgr.filebench.server.component;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDNameGeneratorAdapter implements NameGenerator {

    @Override
    public String name() {
        return UUID.randomUUID().toString();
    }

}
