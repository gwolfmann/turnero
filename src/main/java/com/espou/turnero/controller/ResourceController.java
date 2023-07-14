package com.espou.turnero.controller;

import com.espou.turnero.service.ResourceServiceOld;
import com.espou.turnero.storage.ResourceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceServiceOld resourceServiceOld;

    private final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    public ResourceController(ResourceServiceOld resourceServiceOld) {
//        this.resourcePipeline = new ResourcePipeline();
        this.resourceServiceOld = new ResourceServiceOld();
    }

/*
    public Mono<ServerResponse> getAllResources() {
        logger.info("Received GET request for all resources");
        return resourceService.getAllResources()
                .collectList()
                .flatMap(x -> ServerResponse.ok().bodyValue(x));
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> getResourceById(@PathVariable String id) {
        logger.info("Received GET request for resource with ID: {}", id);
        return resourceServiceOld.getResourceByInternalId(id);
    }
*/
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResourceDTO> createResource(@RequestBody ResourceDTO resourceDTO) {
        logger.info("Received POST request to create a new resource");
        return resourceServiceOld.createResource(resourceDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> updateResource(@PathVariable String id, @RequestBody ResourceDTO resourceDTO) {
        logger.info("Received PUT request to update resource with ID: {}", id);
        return resourceServiceOld.updateResourceByInternalId(id, resourceDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteResource(@PathVariable String id) {
        logger.info("Received DELETE request for resource with ID: {}", id);
        return resourceServiceOld.deleteResourceByInternalId(id);
    }
    @GetMapping(value = "/byMongoId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> getResourceByMongoId(@PathVariable String id) {
        logger.info("Received GET request for resource with ID: {}", id);
        return resourceServiceOld.getResourceById(id);
    }

    @PutMapping(value = "/byMongoId/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> updateResourceByMongoId(@PathVariable String id, @RequestBody ResourceDTO resourceDTO) {
        logger.info("Received PUT request to update resource with ID: {}", id);
        return resourceServiceOld.updateResource(id, resourceDTO);
    }

    @DeleteMapping("/byMongoId/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteResourceByMongoId(@PathVariable String id) {
        logger.info("Received DELETE request for resource with ID: {}", id);
        return resourceServiceOld.deleteResource(id);
    }
}
