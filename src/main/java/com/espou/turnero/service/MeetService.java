package com.espou.turnero.service;

import com.espou.turnero.storage.MeetDTO;
import com.espou.turnero.storage.MeetDTOLookup;
import com.espou.turnero.storage.MeetLookupRepository;
import com.espou.turnero.storage.MeetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class MeetService {
    private final MeetRepository meetRepository;
    private final MeetLookupRepository meetLookupRepository;

    @Autowired
    public MeetService(MeetRepository meetRepository,MeetLookupRepository meetLookupRepository) {
        this.meetRepository = meetRepository;
        this.meetLookupRepository = meetLookupRepository;
    }

    public Flux<MeetDTOLookup> getAllMeets(ServerRequest serverRequest) {
        Optional<String> resourceInternalId = serverRequest.queryParam("resource");
        Optional<String> providerInternalId = serverRequest.queryParam("provider");
        if (resourceInternalId.isPresent() || providerInternalId.isPresent()) {
            return meetLookupRepository.getMeets(resourceInternalId.orElse(""), providerInternalId.orElse(""));
        }
        return meetLookupRepository.getMeets();
    }

    public Mono<MeetDTO> getMeetById(String id) {
        return meetRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Meet not found with id: " + id)));
    }

    public Mono<MeetDTO> getMeetByInternalId(String internalId) {
        return meetRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Meet not found with internalId: " + internalId)));
    }

    public Mono<MeetDTO> createMeet(MeetDTO meetDTO) {
        return meetRepository.save(meetDTO);
    }

    public Mono<MeetDTO> updateMeet(MeetDTO meetDTO, String internalId) {
        return meetRepository.findByInternalId(internalId)
                .flatMap(existingMeet -> {
                    meetDTO.setId(existingMeet.getId());
                    return meetRepository.save(meetDTO);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Meet not found with internalId: " + internalId)));
    }

    public Mono<MeetDTO> deleteMeet(String internalId) {
        return meetRepository.findByInternalId(internalId)
                .flatMap(existingMeet -> meetRepository.delete(existingMeet)
                        .thenReturn(existingMeet))
                .switchIfEmpty(Mono.error(new RuntimeException("Meet not found with internalId: " + internalId)));
    }
}

/*
@Service
public class MeetService {
    private final MeetRepositoryOld meetRepositoryOld;
    private final ResourceServiceOld resourceServiceOld;
    private final ProviderService providerService;

    public MeetService(MeetRepositoryOld meetRepositoryOld, ResourceServiceOld resourceServiceOld, ProviderService providerService) {
        this.meetRepositoryOld = meetRepositoryOld;
        this.resourceServiceOld = resourceServiceOld;
        this.providerService = providerService;
    }

    public Mono<MeetDTO> createMeet(MeetDTO meetDTO) {
        String resourceId = meetDTO.getResource().getId();
        String providerId = meetDTO.getProvider().getId();

        return resourceServiceOld.getTimelineById(resourceId)
            .flatMap(resourceTimeline -> providerService.getProviderById(providerId)
                .map(ProviderDTO::getTimeline)
                .flatMap(providerTimeline -> {
                    boolean isResourceInRange = isMeetOnRange(meetDTO, resourceTimeline);
                    boolean isProviderInRange = isMeetOnRange(meetDTO, providerTimeline);

                    if (!isResourceInRange) {
                        return Mono.error(new IllegalArgumentException("Meet is outside the resource's timeline."));
                    }

                    if (!isProviderInRange) {
                        return Mono.error(new IllegalArgumentException("Meet is outside the provider's timeline."));
                    }

                    return getOverlappingMeets(resourceId, providerId, meetDTO.getDate(), meetDTO.getHour(), meetDTO.getEndTime())
                        .collectList()
                        .flatMap(existingMeets -> {
                            if (existingMeets.isEmpty()) {
                                return meetRepositoryOld.save(meetDTO);
                            } else {
                                return Mono.error(new IllegalArgumentException("Meet overlaps with existing meets."));
                            }
                        });
                })
                );
    }

    private boolean isMeetOnRange(MeetDTO meetDTO, TimeLine timeline) {
        return timeline.isMeetOnRange(meetDTO);
    }

    private Flux<MeetDTO> getOverlappingMeets(String resourceId, String providerId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return meetRepositoryOld.findByResourceAndProvider(resourceId, providerId)
            .filter(existingMeet ->
                date.equals(existingMeet.getDate()) &&
                    !(endTime.isBefore(existingMeet.getHour()) || startTime.isAfter(existingMeet.getEndTime()))
            );
    }

    public Mono<MeetDTO> getMeetById(String id) {
        return meetRepositoryOld.findById(id);
    }

    public Mono<MeetDTO> updateMeet(String id, MeetDTO meetDTO) {
        return meetRepositoryOld.findById(id)
                .flatMap(existingMeet -> {
                    existingMeet.setResource(meetDTO.getResource());
                    existingMeet.setProvider(meetDTO.getProvider());
                    existingMeet.setReceiver(meetDTO.getReceiver());
                    existingMeet.setTask(meetDTO.getTask());
                    existingMeet.setDate(meetDTO.getDate());
                    existingMeet.setHour(meetDTO.getHour());
                    existingMeet.setDuration(meetDTO.getDuration());
                    existingMeet.setInternalId(meetDTO.getInternalId());
                    return meetRepositoryOld.save(existingMeet);
                });
    }

    public Mono<Void> deleteMeet(String id) {
        return meetRepositoryOld.deleteById(id);
    }
    public Flux<MeetDTO> getMeetsByResource(String resourceId) {
        return meetRepositoryOld.findByResource_Id(resourceId);
    }

    public Flux<MeetDTO> getMeetsByProvider(String providerId) {
        return meetRepositoryOld.findByProvider_Id(providerId);
    }
*/