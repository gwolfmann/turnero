package com.espou.turnero.storage;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ResourceRepository extends ReactiveMongoRepository<ResourceDTO, String> {
    @Query("{ internalId: ?0 }")
    Mono<ResourceDTO> findByInternalId(String internalId);
}
