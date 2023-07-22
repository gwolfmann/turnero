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

    public Mono<ResourceDTO> writeResource(ResourceDTO resourceDTO){
        return resourceRepository.save(resourceDTO);
    }
    public Mono<ResourceDTO> updateResource(ResourceDTO resourceDTO, String internalId){
        return resourceRepository.findByInternalId(internalId)
            .flatMap(existingResource -> updateId(resourceDTO,existingResource.getId()))
            .flatMap(resourceRepository::save)
            .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Resource by internalId "+internalId) ));
    }

    public Mono<ResourceDTO> deleteResource(String internalId){
        return resourceRepository.deleteByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Resource by internalId "+internalId) ));
    }

    private Mono<ResourceDTO> updateId(ResourceDTO resourceDTO, String id){
        resourceDTO.setId(id);
        return Mono.just(resourceDTO);
    }
    private ResourceDTO representsDeleted(ResourceDTO resourceDTO){
        resourceDTO.setName("deleted value");
        return resourceDTO;
    }
}
