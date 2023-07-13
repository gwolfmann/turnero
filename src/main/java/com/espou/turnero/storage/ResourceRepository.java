package com.espou.turnero.storage;

import com.espou.turnero.MongoReactiveApplication;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ResourceRepository {
    @Autowired
    private ReactiveMongoTemplate mongoTemplate = MongoReactiveApplication.g;

//    public ResourceRepository(ReactiveMongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }

    public Mono<ResourceDTO> save(ResourceDTO resource) {
        return mongoTemplate.save(resource);
    }

    public Mono<ResourceDTO> findById(String id) {
        return mongoTemplate.findById(id, ResourceDTO.class);
    }

    public Mono<ResourceDTO> findByInternalId(String internalId) {
        return mongoTemplate.findOne(Query.query(Criteria.where("internalId").is(internalId)), ResourceDTO.class);
    }

    public Flux<ResourceDTO> findAll() {
        return mongoTemplate.findAll(ResourceDTO.class);
    }

    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), ResourceDTO.class)
                .then();
    }

    public Mono<Void> deleteByInternalId(String internalId) {
        return mongoTemplate.remove(Query.query(Criteria.where("internalId").is(internalId)), ResourceDTO.class)
                .then();
    }

    public Mono<ResourceDTO> updateById(String id, ResourceDTO updatedResource) {
        return mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(id)), updatedResource)
                .flatMap(result -> Mono.just(updatedResource))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Resource not found")));
    }

}
