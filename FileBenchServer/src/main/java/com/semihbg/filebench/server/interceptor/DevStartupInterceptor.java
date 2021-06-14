package com.semihbg.filebench.server.interceptor;

import com.semihbg.filebench.server.source.LocalFileSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class DevStartupInterceptor implements CommandLineRunner {

    private final LocalFileSource localFileSource;

    @Override
    public void run(String... args) {
        log.info("Profile 'dev' start up interceptor is started");
        localFileSource.createRootFolderIfNotExists();
        log.info("Profile 'dev' start up interceptor is done");
    }

}
