package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProviderRepository extends ReactiveMongoRepository<ProviderDTO, String> {
    @Query("{ internalId: ?0 }")
    Mono<ProviderDTO> findByInternalId(String internalId);
    Mono<ProviderDTO> deleteByInternalId(String internalId);
}
