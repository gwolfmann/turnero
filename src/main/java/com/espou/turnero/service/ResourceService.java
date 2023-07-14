package com.espou.turnero.service;

import com.espou.turnero.storage.ResourceDTO;
import com.espou.turnero.storage.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Flux<ResourceDTO> getAllResources() {
        return resourceRepository.findAll();
    }

    public Mono<ResourceDTO> getResourceById(String id) {
        return resourceRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Resource by id "+id) ));
    }

    public Mono<ResourceDTO> getResourceByInternalId(String internalId) {
        return resourceRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Resource by internalId "+internalId) ));
    }
}
