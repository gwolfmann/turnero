package com.espou.turnero.storage;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MeetLookupRepository extends ReactiveMongoRepository<MeetDTOLookup, String> {

    @Aggregation(pipeline = {
            "{$lookup: { from: 'providers', localField: 'providerInternalId', foreignField: 'internalId', as: 'provider' }}",
            "{$unwind: { path: '$provider', preserveNullAndEmptyArrays: true }}",
            "{$lookup: { from: 'resources', localField: 'resourceInternalId', foreignField: 'internalId', as: 'resource' }}",
            "{$unwind: { path: '$resource', preserveNullAndEmptyArrays: true }}",
            "{$lookup: { from: 'receivers', localField: 'receiverInternalId', foreignField: 'internalId', as: 'receiver' }}",
            "{$unwind: { path: '$receiver', preserveNullAndEmptyArrays: true }}",
            "{$match: { $or: [ { $expr: { $eq: ['$provider.internalId', ?0] } }, { $expr: { $eq: ['$resource.internalId', ?1]}}]}}"
    })
    Flux<MeetDTOLookup> getMeets(String providerInternalId, String resourceInternalId);

    @Aggregation(pipeline = {
            "{$lookup: { from: 'providers', localField: 'providerInternalId', foreignField: 'internalId', as: 'provider' }}",
            "{$unwind: { path: '$provider', preserveNullAndEmptyArrays: true }}",
            "{$lookup: { from: 'resources', localField: 'resourceInternalId', foreignField: 'internalId', as: 'resource' }}",
            "{$unwind: { path: '$resource', preserveNullAndEmptyArrays: true }}",
            "{$lookup: { from: 'receivers', localField: 'receiverInternalId', foreignField: 'internalId', as: 'receiver' }}",
            "{$unwind: { path: '$receiver', preserveNullAndEmptyArrays: true }}",
    })
    Flux<MeetDTOLookup> getMeets();
}
