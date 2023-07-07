package com.espou.turnero.storage;


import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProviderRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public ProviderRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<ProviderDTO> save(ProviderDTO provider) {
        return mongoTemplate.save(provider);
    }

    public Mono<ProviderDTO> findById(String id) {
        return mongoTemplate.findById(id, ProviderDTO.class);
    }

}
