package com.espou.turnero.service;
import com.espou.turnero.model.TimeLine;
import com.espou.turnero.storage.ResourceDTO;
import com.espou.turnero.storage.ResourceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Flux<ResourceDTO> getAllResources() {
        return resourceRepository.findAll();
    }

    public Mono<ResourceDTO> getResourceById(String id) {
        return resourceRepository.findById(id);
    }
    public Mono<ResourceDTO> getResourceByInternalId(String internalId) {
        return resourceRepository.findByInternalId(internalId);
    }

    public Mono<TimeLine> getTimelineById(String id) {
        return  this.getResourceById(id).map(ResourceDTO::getTimeline);
    }
    public Mono<TimeLine> getTimelineByInternalId(String internalId) {
        return resourceRepository.findByInternalId(internalId).map(ResourceDTO::getTimeline);
    }

    public Mono<ResourceDTO> createResource(ResourceDTO resourceDTO) {
        return resourceRepository.save(resourceDTO);
    }

    public Mono<ResourceDTO> updateResource(String id, ResourceDTO resourceDTO) {
        return resourceRepository.findById(id)
                .flatMap(existingResource -> {
                    existingResource.setName(resourceDTO.getName());
                    existingResource.setInternalId(resourceDTO.getInternalId());
                    existingResource.setTimeline(resourceDTO.getTimeline());
                    return resourceRepository.save(existingResource);
                });
    }

    public Mono<ResourceDTO> updateResourceByInternalId(String internalId, ResourceDTO resourceDTO) {
        return resourceRepository.findByInternalId(internalId)
                .flatMap(existingResource -> {
                    existingResource.setName(resourceDTO.getName());
                    existingResource.setInternalId(resourceDTO.getInternalId());
                    existingResource.setTimeline(resourceDTO.getTimeline());
                    return resourceRepository.save(existingResource);
                });
    }
    public Mono<Void> deleteResource(String id) {
        return resourceRepository.deleteById(id);
    }


    public Mono<Void> deleteResourceByInternalId(String internalId) {
        return resourceRepository.deleteByInternalId(internalId);
    }
}
