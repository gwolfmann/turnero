package com.espou.turnero.service;

import com.espou.turnero.model.TimeLine;
import com.espou.turnero.storage.MeetDTO;
import com.espou.turnero.storage.MeetRepository;
import com.espou.turnero.storage.ProviderDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class MeetService {
    private final MeetRepository meetRepository;
    private final ResourceServiceOld resourceServiceOld;
    private final ProviderService providerService;

    public MeetService(MeetRepository meetRepository, ResourceServiceOld resourceServiceOld, ProviderService providerService) {
        this.meetRepository = meetRepository;
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
                                return meetRepository.save(meetDTO);
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
        return meetRepository.findByResourceAndProvider(resourceId, providerId)
            .filter(existingMeet ->
                date.equals(existingMeet.getDate()) &&
                    !(endTime.isBefore(existingMeet.getHour()) || startTime.isAfter(existingMeet.getEndTime()))
            );
    }

    public Mono<MeetDTO> getMeetById(String id) {
        return meetRepository.findById(id);
    }

    public Mono<MeetDTO> updateMeet(String id, MeetDTO meetDTO) {
        return meetRepository.findById(id)
                .flatMap(existingMeet -> {
                    existingMeet.setResource(meetDTO.getResource());
                    existingMeet.setProvider(meetDTO.getProvider());
                    existingMeet.setReceiver(meetDTO.getReceiver());
                    existingMeet.setTask(meetDTO.getTask());
                    existingMeet.setDate(meetDTO.getDate());
                    existingMeet.setHour(meetDTO.getHour());
                    existingMeet.setDuration(meetDTO.getDuration());
                    existingMeet.setInternalId(meetDTO.getInternalId());
                    return meetRepository.save(existingMeet);
                });
    }

    public Mono<Void> deleteMeet(String id) {
        return meetRepository.deleteById(id);
    }
    public Flux<MeetDTO> getMeetsByResource(String resourceId) {
        return meetRepository.findByResource_Id(resourceId);
    }

    public Flux<MeetDTO> getMeetsByProvider(String providerId) {
        return meetRepository.findByProvider_Id(providerId);
    }
}