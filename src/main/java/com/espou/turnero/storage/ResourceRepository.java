package com.espou.turnero.storage;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ResourceRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public ResourceRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<ResourceDTO> save(ResourceDTO resource) {
        return mongoTemplate.save(resource);
    }

    public Mono<ResourceDTO> findById(String id) {
        return mongoTemplate.findById(id, ResourceDTO.class);
    }

    public Flux<ResourceDTO> findAll() {
        return mongoTemplate.findAll(ResourceDTO.class);
    }

    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), ResourceDTO.class)
                .then();
    }

    // Add more custom repository methods as needed
}
