package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MeetRepository extends ReactiveMongoRepository<MeetDTO, String> {
    Mono<MeetDTO> findByInternalId(String internalId);
}
