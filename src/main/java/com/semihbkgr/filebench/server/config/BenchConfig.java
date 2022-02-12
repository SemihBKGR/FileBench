package com.semihbkgr.filebench.server.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class BenchConfig {

    @Bean
    @ConfigurationProperties(prefix = "bench", ignoreUnknownFields = false)
    public BenchProperties benchProperties() {
        return new BenchProperties();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BenchProperties {

        private int maxFileCount;

        private int maxFileSize;

        private long minExpirationDuration;

        private long maxExpirationDuration;

        private FileProperties file;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class FileProperties {

            private int maxSize;

        }

    }

}
