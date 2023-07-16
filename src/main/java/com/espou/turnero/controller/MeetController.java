package com.espou.turnero.controller;
import com.espou.turnero.service.MeetService;
import com.espou.turnero.storage.MeetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/meets")
public class MeetController {
    private final MeetService meetService;
    private final Logger logger = LoggerFactory.getLogger(MeetController.class);

    public MeetController(MeetService meetService) {
        this.meetService = meetService;
    }
/*

    @GetMapping(value = "/resource/{resourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MeetDTO> getMeetsByResource(@PathVariable String resourceId) {
        return meetService.getMeetsByResource(resourceId);
    }

    @GetMapping(value = "/provider/{providerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MeetDTO> getMeetsByProvider(@PathVariable String providerId) {
        return meetService.getMeetsByProvider(providerId);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MeetDTO> getMeetById(@PathVariable String id) {
        logger.info("Received GET request for meet with ID: {}", id);
        return meetService.getMeetById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MeetDTO> createMeet(@RequestBody MeetDTO meetDTO) {
        logger.info("Received POST request to create a new meet");
        return meetService.createMeet(meetDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MeetDTO> updateMeet(@PathVariable String id, @RequestBody MeetDTO meetDTO) {
        logger.info("Received PUT request to update meet with ID: {}", id);
        return meetService.updateMeet(id, meetDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMeet(@PathVariable String id) {
        logger.info("Received DELETE request for meet with ID: {}", id);
        return meetService.deleteMeet(id);
    }
*/
}
