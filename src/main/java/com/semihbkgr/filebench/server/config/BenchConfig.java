package com.semihbkgr.filebench.server.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class BenchConfig {

    @Bean
    @ConfigurationProperties(prefix = "bench")
    public BenchProperties benchProperties() {
        return new BenchProperties();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BenchProperties {

        public static final long DEFAULT_MAX_SIZE = 50000000L;
        public static final int DEFAULT_MAX_FILE_COUNT = 25;
        public static final long DEFAULT_MAX_FILE_SIZE = 5000000L;
        public static final long DEFAULT_MIN_EXPIRATION_DURATION = 100000L;
        public static final long DEFAULT_MAX_EXPIRATION_DURATION = 1000000L;

        private long maxSize;

        private int maxFileCount = DEFAULT_MAX_FILE_COUNT;

        private long maxFileSize = DEFAULT_MAX_FILE_SIZE;

        private long minExpirationDuration = DEFAULT_MIN_EXPIRATION_DURATION;

        private long maxExpirationDuration = DEFAULT_MAX_EXPIRATION_DURATION;

        private FileProperties file;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class FileProperties {

            public static final long DEFAULT_MAX_SIZE = 1000000L;

            private long maxSize = DEFAULT_MAX_SIZE;

        }

    }

}
