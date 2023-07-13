package com.espou.turnero.controller;

import com.espou.turnero.service.ResourceService;
import com.espou.turnero.storage.ResourceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;
    private final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    public Mono<ServerResponse> getAllResources() {
        logger.info("Received GET request for all resources");
        return resourceService.getAllResources()
                .collectList()
                .flatMap(x -> ServerResponse.ok().bodyValue(x));

    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> getResourceById(@PathVariable String id) {
        logger.info("Received GET request for resource with ID: {}", id);
        return resourceService.getResourceByInternalId(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResourceDTO> createResource(@RequestBody ResourceDTO resourceDTO) {
        logger.info("Received POST request to create a new resource");
        return resourceService.createResource(resourceDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> updateResource(@PathVariable String id, @RequestBody ResourceDTO resourceDTO) {
        logger.info("Received PUT request to update resource with ID: {}", id);
        return resourceService.updateResourceByInternalId(id, resourceDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteResource(@PathVariable String id) {
        logger.info("Received DELETE request for resource with ID: {}", id);
        return resourceService.deleteResourceByInternalId(id);
    }
    @GetMapping(value = "/byMongoId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> getResourceByMongoId(@PathVariable String id) {
        logger.info("Received GET request for resource with ID: {}", id);
        return resourceService.getResourceById(id);
    }

    @PutMapping(value = "/byMongoId/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResourceDTO> updateResourceByMongoId(@PathVariable String id, @RequestBody ResourceDTO resourceDTO) {
        logger.info("Received PUT request to update resource with ID: {}", id);
        return resourceService.updateResource(id, resourceDTO);
    }

    @DeleteMapping("/byMongoId/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteResourceByMongoId(@PathVariable String id) {
        logger.info("Received DELETE request for resource with ID: {}", id);
        return resourceService.deleteResource(id);
    }
}
