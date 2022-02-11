package com.semihbkgr.filebench.server.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.util.Optional;

@Configuration
@EnableR2dbcRepositories(basePackages = {"com.semihbkgr.filebench.server.model"})
public class R2dbcConfig {

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory,
                                                    @Value("${spring.r2dbc.sql.schema}") Optional<String> schemaFileOptional) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        schemaFileOptional.ifPresent(schema ->
                initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource(schema))));
        return initializer;
    }

}
