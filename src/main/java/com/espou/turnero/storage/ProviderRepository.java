package com.espou.turnero.storage;


import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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

    public Mono<ProviderDTO> findByInternalId(String internalId) {
        return mongoTemplate.findOne(Query.query(Criteria.where("internalId").is(internalId)), ProviderDTO.class);
    }

    public Flux<ProviderDTO> findAll() {
        return mongoTemplate.findAll(ProviderDTO.class);
    }

    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), ProviderDTO.class)
                .then();
    }

    public Mono<Void> deleteByInternalId(String internalId) {
        return findByInternalId(internalId)
                .flatMap(provider -> deleteById(provider.getId()));
    }

    public Mono<ProviderDTO> updateById(String id, ProviderDTO updatedProvider) {
        return mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(id)), updatedProvider)
                .flatMap(result -> Mono.just(updatedProvider))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Provider not found")));
    }
}
