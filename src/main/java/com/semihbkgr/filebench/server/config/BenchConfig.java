package com.semihbkgr.filebench.server.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
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
        public static final long DEFAULT_MIN_EXPIRATION_DURATION_MS = 100000L;
        public static final long DEFAULT_MAX_EXPIRATION_DURATION_MS = 1000000L;

        @JsonProperty("max_size")
        private long maxSize;

        @JsonProperty("max_file_count")
        private int maxFileCount = DEFAULT_MAX_FILE_COUNT;

        @JsonProperty("max_file_size")
        private long maxFileSize = DEFAULT_MAX_FILE_SIZE;

        @JsonProperty("max_expiration_duration")
        private Duration minExpirationDuration = Duration.ofMillis(DEFAULT_MIN_EXPIRATION_DURATION_MS);

        @JsonProperty("min_expiration_duration")
        private Duration maxExpirationDuration =  Duration.ofMillis(DEFAULT_MAX_EXPIRATION_DURATION_MS);

        private FileProperties file;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class FileProperties {

            public static final long DEFAULT_MAX_SIZE = 1000000L;

            @JsonProperty("max_size")
            private long maxSize = DEFAULT_MAX_SIZE;

        }

    }

}
