package com.semihbg.filebench.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.semihbg.filebench.server.repository"})
@EnableReactiveMongoAuditing
public class MongoConfig {
}