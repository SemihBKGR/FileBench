package com.semihbkgr.filebench.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.semihbkgr.filebench.server.repository"})
public class MongoConfig {

}
