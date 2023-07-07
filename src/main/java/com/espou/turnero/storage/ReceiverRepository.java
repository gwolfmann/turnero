package com.espou.turnero.storage;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
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

    // Add more repository methods as needed
}
