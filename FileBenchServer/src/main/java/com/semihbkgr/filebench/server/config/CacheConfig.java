package com.semihbkgr.filebench.server.config;

import com.hazelcast.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Config hazelcastConfig(@Value("hazelcast.instance:instance") String hazelcastInstance) {
        return new Config().setInstanceName(hazelcastInstance);
    }

}
