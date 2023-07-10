package com.espou.turnero.storage;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ReceiverRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    public ReceiverRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<ReceiverDTO> save(ReceiverDTO receiver) {
        return mongoTemplate.save(receiver);
    }

    public Mono<ReceiverDTO> findById(String id) {
        return mongoTemplate.findById(id, ReceiverDTO.class);
    }
    public Mono<ReceiverDTO> findByInternalId(String internalId) {
        return mongoTemplate.findOne(Query.query(Criteria.where("internalId").is(internalId)), ReceiverDTO.class);
    }

    public Flux<ReceiverDTO> findAll() {
        return mongoTemplate.findAll(ReceiverDTO.class);
    }

    public Mono<Void> deleteById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), ReceiverDTO.class)
                .then();
    }

    public Mono<Void> deleteByInternalId(String internalId) {
        return mongoTemplate.remove(Query.query(Criteria.where("internalId").is(internalId)), ReceiverDTO.class)
                .then();
    }

    public Mono<ReceiverDTO> updateById(String id, ReceiverDTO updatedResource) {
        return mongoTemplate.findAndReplace(Query.query(Criteria.where("_id").is(id)), updatedResource)
                .flatMap(result -> Mono.just(updatedResource))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Receiver not found")));
    }

}