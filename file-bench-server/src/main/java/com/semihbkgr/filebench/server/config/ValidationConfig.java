package com.semihbkgr.filebench.server.config;

import com.semihbkgr.filebench.server.validation.BenchConstraints;
import com.semihbkgr.filebench.server.validation.FileConstraints;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class ValidationConfig {

    @Bean
    @ConfigurationProperties("validation.bench")
    public BenchConstraints benchConstraints() {
        return new BenchConstraints();
    }

    @Bean
    @ConfigurationProperties("validation.file")
    public FileConstraints fileConstraints() {
        return new FileConstraints();
    }

}
