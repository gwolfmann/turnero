package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MeetRepository extends ReactiveMongoRepository<MeetDTO, String> {
    Mono<MeetDTO> findByInternalId(String internalId);

    @Aggregation(pipeline = {
            "{$lookup: { from: 'providers', localField: 'providerInternalId', foreignField: 'internalId', as: 'provider' }}",
            "{$lookup: { from: 'resources', localField: 'resourceInternalId', foreignField: 'internalId', as: 'resource' }}",
            "{$lookup: { from: 'receivers', localField: 'receiverInternalId', foreignField: 'internalId', as: 'receiver' }}",
//        "{$unwind: {path: '$providers', preserveNullAndEmptyArrays: true}}",
//        "{$addFields: {providerName: '$provider.name', providerTimeline: '$providers.timeline'}}",
//        "{$project: {providers: 0}}"
    })
    Flux<MeetDTO> getMeetsWithProviders();
}
