package com.espou.turnero;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories
@Profile("local")
public class MongoReactiveApplication extends AbstractReactiveMongoConfiguration {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTemplate(factory);
    }

    @Override
    protected String getDatabaseName() {
        return "turnero";
    }
}
