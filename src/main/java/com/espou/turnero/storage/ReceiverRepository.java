package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReceiverRepository extends ReactiveMongoRepository<ReceiverDTO, String> {
    @Query("{ internalId: ?0 }")
    Mono<ReceiverDTO> findByInternalId(String internalId);

}
