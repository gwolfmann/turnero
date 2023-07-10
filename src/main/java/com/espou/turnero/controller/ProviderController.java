package com.espou.turnero.controller;

import com.espou.turnero.service.ProviderService;
import com.espou.turnero.storage.ProviderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/providers")
public class ProviderController {
    private final ProviderService providerService;
    private final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ProviderDTO> getAllProviders() {
        logger.info("Received GET request for all providers");
        return providerService.getAllProviders();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProviderDTO> getProviderById(@PathVariable String id) {
        logger.info("Received GET request for provider with ID: {}", id);
        return providerService.getProviderByInternalId(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProviderDTO> createProvider(@RequestBody ProviderDTO providerDTO) {
        logger.info("Received POST request to create a new provider");
        return providerService.createProvider(providerDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProviderDTO> updateProvider(@PathVariable String id, @RequestBody ProviderDTO providerDTO) {
        logger.info("Received PUT request to update provider with ID: {}", id);
        return providerService.updateProviderByInternalId(id, providerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProvider(@PathVariable String id) {
        logger.info("Received DELETE request for provider with ID: {}", id);
        return providerService.deleteProviderByInternalId(id);
    }

    @GetMapping(value = "/byMongoId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProviderDTO> getProviderByMongoId(@PathVariable String id) {
        logger.info("Received GET request for provider with ID: {}", id);
        return providerService.getProviderById(id);
    }

    @PutMapping(value = "/byMongoId/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProviderDTO> updateProviderByMongoId(@PathVariable String id, @RequestBody ProviderDTO providerDTO) {
        logger.info("Received PUT request to update provider with ID: {}", id);
        return providerService.updateProvider(id, providerDTO);
    }

    @DeleteMapping("/byMongoId/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProviderByMongoId(@PathVariable String id) {
        logger.info("Received DELETE request for provider with ID: {}", id);
        return providerService.deleteProvider(id);
    }
}
