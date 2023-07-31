package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface MeetRepository extends ReactiveMongoRepository<MeetDTO, String> {
    Mono<MeetDTO> findByInternalId(String internalId);

    @Aggregation(pipeline = {
            "{$lookup: { from: 'providers', localField: 'providerInternalId', foreignField: 'internalId', as: 'provider' }}",
            "{$lookup: { from: 'resources', localField: 'resourceInternalId', foreignField: 'internalId', as: 'resource' }}",
            "{$lookup: { from: 'receivers', localField: 'receiverInternalId', foreignField: 'internalId', as: 'receiver' }}",
    })
    Flux<MeetDTO> getMeetsWithProviders();

    Flux<MeetDTO> findByResourceInternalIdAndProviderInternalIdAndDate(String resourceInternalId, String providerInternalId, LocalDate date);
    Mono<MeetDTO> deleteByInternalId(String internalId);

}
