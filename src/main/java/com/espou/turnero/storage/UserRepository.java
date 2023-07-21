package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDTO, String> {
    @Query("{ internalId: ?0 }")
    Mono<UserDTO> findByInternalId(String internalId);

    //todo implementar los deletes
    Mono<UserDTO> deleteByInternalId(String internalId);
}
