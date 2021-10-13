package com.semihbkgr.filebench.server.config;

import com.semihbkgr.filebench.server.validation.BenchConstraint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties
public class ValidationConfig {

    @Bean
    @Profile("dev")
    @ConfigurationProperties("validation.bench")
    public BenchConstraint benchConstraint(){
        return new BenchConstraint();
    }

}
