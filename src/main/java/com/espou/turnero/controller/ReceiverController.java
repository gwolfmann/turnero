package com.espou.turnero.controller;

import com.espou.turnero.service.ReceiverService;
import com.espou.turnero.storage.ReceiverDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/receivers")
public class ReceiverController {
    private final ReceiverService ReceiverService;
    private final Logger logger = LoggerFactory.getLogger(ReceiverController.class);

    public ReceiverController(ReceiverService ReceiverService) {
        this.ReceiverService = ReceiverService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ReceiverDTO> getAllReceivers() {
        logger.info("Received GET request for all Receivers");
        return ReceiverService.getAllReceivers();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ReceiverDTO> getReceiverById(@PathVariable String id) {
        logger.info("Received GET request for Receiver with ID: {}", id);
        return ReceiverService.getReceiverByInternalId(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ReceiverDTO> createReceiver(@RequestBody ReceiverDTO ReceiverDTO) {
        logger.info("Received POST request to create a new Receiver");
        return ReceiverService.createReceiver(ReceiverDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ReceiverDTO> updateReceiver(@PathVariable String id, @RequestBody ReceiverDTO ReceiverDTO) {
        logger.info("Received PUT request to update Receiver with ID: {}", id);
        return ReceiverService.updateReceiverByInternalId(id, ReceiverDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteReceiver(@PathVariable String id) {
        logger.info("Received DELETE request for Receiver with ID: {}", id);
        return ReceiverService.deleteReceiverByInternalId(id);
    }
    @GetMapping(value = "/byMongoId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ReceiverDTO> getReceiverByMongoId(@PathVariable String id) {
        logger.info("Received GET request for Receiver with ID: {}", id);
        return ReceiverService.getReceiverById(id);
    }

    @PutMapping(value = "/byMongoId/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ReceiverDTO> updateReceiverByMongoId(@PathVariable String id, @RequestBody ReceiverDTO ReceiverDTO) {
        logger.info("Received PUT request to update Receiver with ID: {}", id);
        return ReceiverService.updateReceiver(id, ReceiverDTO);
    }

    @DeleteMapping("/byMongoId/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteReceiverByMongoId(@PathVariable String id) {
        logger.info("Received DELETE request for Receiver with ID: {}", id);
        return ReceiverService.deleteReceiver(id);
    }
}
