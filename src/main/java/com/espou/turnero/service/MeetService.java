package com.espou.turnero.service;
import com.espou.turnero.model.TimeLine;
import com.espou.turnero.storage.MeetDTO;
import com.espou.turnero.storage.MeetRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MeetService {
    private final MeetRepository meetRepository;
    private final ResourceService resourceService;
    private final ProviderService providerService;

    public MeetService(MeetRepository meetRepository, ResourceService resourceService, ProviderService providerService) {
        this.meetRepository = meetRepository;
        this.resourceService = resourceService;
        this.providerService = providerService;
    }

    public Mono<MeetDTO> createMeet(MeetDTO meetDTO) {
        String resourceId = meetDTO.getResource().getId();
        String providerId = meetDTO.getProvider().getId();

        // Check if the meet is within the resource's timeline
        boolean isResourceInRange = isMeetOnRange(meetDTO, ResourceType.RESOURCE, resourceId);
        if (!isResourceInRange) {
            return Mono.error(new IllegalArgumentException("Meet is outside the resource's timeline."));
        }

        // Check if the meet is within the provider's timeline
        boolean isProviderInRange = isMeetOnRange(meetDTO, ResourceType.PROVIDER, providerId);
        if (!isProviderInRange) {
            return Mono.error(new IllegalArgumentException("Meet is outside the provider's timeline."));
        }

        // Check for overlapping meets
        return getOverlappingMeets(resourceId, providerId, meetDTO.getDate(), meetDTO.getHour(), meetDTO.getEndTime())
                .collectList()
                .flatMap(existingMeets -> {
                    if (existingMeets.isEmpty()) {
                        return meetRepository.save(meetDTO);
                    } else {
                        return Mono.error(new IllegalArgumentException("Meet overlaps with existing meets."));
                    }
                });
    }

    private boolean isMeetOnRange(MeetDTO meetDTO, ResourceType type, String id) {
        TimeLine timeline = getTimelineById(id, type);
        return timeline.isMeetOnRange(meetDTO);
    }

    private TimeLine getTimelineById(String id, ResourceType type) {
        // Retrieve the timeline based on the ID and type
        // Replace this with the actual implementation to fetch the timeline
        return (type == ResourceType.RESOURCE) ? resourceService.getTimelineById(id) : providerService.getTimelineById(id);
    }

    private Flux<MeetDTO> getOverlappingMeets(String resourceId, String providerId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return meetRepository.findByResourceAndProvider(resourceId, providerId)
                .filter(existingMeet ->
                        date.equals(existingMeet.getDate()) &&
                                !(endTime.isBefore(existingMeet.getHour()) || startTime.isAfter(existingMeet.getEndTime()))
                );
    }

    // Add more service methods as needed
}

enum ResourceType {
    RESOURCE,
    PROVIDER
}