package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<TaskDTO, String> {
    @Query("{ internalId: ?0 }")
    Mono<TaskDTO> findByInternalId(String internalId);
}