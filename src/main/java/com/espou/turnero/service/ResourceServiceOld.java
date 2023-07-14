package com.espou.turnero.service;

import com.espou.turnero.model.TimeLine;
import com.espou.turnero.storage.ResourceDTO;
import com.espou.turnero.storage.ResourceRepositoryOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
public class ResourceServiceOld {

    @Autowired
    private ResourceRepositoryOld resourceRepositoryOld;


    //public ResourceService(ResourceRepository resourceRepository) {
    public ResourceServiceOld() {
        this.resourceRepositoryOld = new ResourceRepositoryOld();
    }


    public Flux<ResourceDTO> getAllResources() {
        return resourceRepositoryOld.findAll();
    }

    public Mono<ResourceDTO> getResourceById(String id) {
        return resourceRepositoryOld.findById(id);
    }
    public Mono<ResourceDTO> getResourceByInternalId(String internalId) {
        return resourceRepositoryOld.findByInternalId(internalId);
    }

    public Mono<TimeLine> getTimelineById(String id) {
        return  this.getResourceById(id).map(ResourceDTO::getTimeline);
    }
    public Mono<TimeLine> getTimelineByInternalId(String internalId) {
        return resourceRepositoryOld.findByInternalId(internalId).map(ResourceDTO::getTimeline);
    }

    public Mono<ResourceDTO> createResource(ResourceDTO resourceDTO) {
        return resourceRepositoryOld.save(resourceDTO);
    }

    public Mono<ResourceDTO> updateResource(String id, ResourceDTO resourceDTO) {
        return resourceRepositoryOld.updateById(id, resourceDTO);
    }

    public Mono<ResourceDTO> updateResourceByInternalId(String internalId, ResourceDTO resourceDTO) {
        return resourceRepositoryOld.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new NoSuchElementException(internalId+" resource not found")))
                .flatMap(existingResource -> {
                    resourceDTO.setId(existingResource.getId());
                    return resourceRepositoryOld.updateById(existingResource.getId(),resourceDTO);
                });
    }
    public Mono<Void> deleteResource(String id) {
        return resourceRepositoryOld.deleteById(id);
    }

    public Mono<Void> deleteResourceByInternalId(String internalId) {
        return resourceRepositoryOld.deleteByInternalId(internalId);
    }
}
