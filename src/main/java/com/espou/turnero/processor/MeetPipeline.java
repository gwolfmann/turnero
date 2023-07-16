package com.espou.turnero.processor;

import com.espou.turnero.model.Meet;
import com.espou.turnero.service.MeetService;
import com.espou.turnero.storage.MeetDTO;
import com.espou.turnero.storage.MeetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class MeetPipeline {
    private final Pipeline<MeetDTO, Meet, Meet> singleReadPipeline;
    private final Pipeline<List<MeetDTO>, List<Meet>, List<Meet>> listReadPipeline;
    private final Pipeline<MeetDTO, MeetDTO, MeetDTO> singleWritePipeline;

    private final Logger logger = LoggerFactory.getLogger(MeetPipeline.class);

    private final MeetService meetService;

    @Autowired
    public MeetPipeline(MeetService meetService) {
        this.meetService = meetService;
        singleReadPipeline = singleReadPipelineBuilder();
        listReadPipeline = listReadPipelineBuilder();
        singleWritePipeline = writePipelineBuilder();
    }

    public Mono<ServerResponse> getMeetList(ServerRequest serverRequest) {
        logger.info("Received GET request for meet list");
        return listReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getMeetSingle(ServerRequest serverRequest) {
        return singleReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> createMeet(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> updateMeet(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> deleteMeet(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    private Mono<List<Meet>> mapListToMeet(List<MeetDTO> list) {
        return Mono.just(list.stream()
                .map(MeetMapper::toEntity)
                .collect(Collectors.toList()));
    }

    private Pipeline<MeetDTO, Meet, Meet> singleReadPipelineBuilder() {
        return Pipeline.<MeetDTO, Meet, Meet>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleMeet)
                /*TODO
                en el processor agregar las lecturas de los related
                 */
                .boProcessor(x->Mono.just(MeetMapper.toEntity(x)))
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<List<MeetDTO>, List<Meet>, List<Meet>> listReadPipelineBuilder() {
        return Pipeline.<List<MeetDTO>, List<Meet>, List<Meet>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> meetService.getAllMeets().collectList())
                .boProcessor(this::mapListToMeet)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<MeetDTO, MeetDTO, MeetDTO> writePipelineBuilder() {
        return Pipeline.<MeetDTO, MeetDTO, MeetDTO>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(this::validateBody)
                .storageOp(this::writeMeet)
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Mono<MeetDTO> getSingleMeet(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("id")) {
            logger.info("Received GET request for meet with Id: {}", vars.get("id"));
            return meetService.getMeetById(vars.get("id"));
        }
        if (vars.containsKey("internalId")) {
            logger.info("Received GET request for meet with internalId: {}", vars.get("internalId"));
            return meetService.getMeetByInternalId(vars.get("internalId"));
        }
        return Mono.empty();
    }

    private Mono<MeetDTO> writeMeet(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("internalId")) {
            String internalId = vars.get("internalId");
            if (serverRequest.method().equals(HttpMethod.DELETE)) {
                logger.info("Received DELETE request for meet with internalId: {}", internalId);
                return meetService.deleteMeet(internalId);
            } else {
                logger.info("Received PUT request for meet with internalId: {}", internalId);
                return serverRequest.bodyToMono(MeetDTO.class)
                        .flatMap(meetDTO -> meetService.updateMeet(meetDTO, internalId));
            }
        } else {
            logger.info("Received POST request for meet ");
            return serverRequest.bodyToMono(MeetDTO.class)
                    .flatMap(meetService::createMeet);
        }
    }
    private Mono<ServerRequest> validateBody(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MeetDTO.class)
                .flatMap(meetDTO -> {
                    if (isMeetValid(meetDTO)) {
                        return Mono.just(serverRequest);
                    } else {
                        return Mono.error(new RuntimeException("Invalid body in the Meet Post"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Empty request body")));
    }

    private boolean isMeetValid(MeetDTO meet) {
        return meet.getResourceInternalId() != null && !meet.getResourceInternalId().isEmpty()
                && meet.getProviderInternalId() != null && !meet.getProviderInternalId().isEmpty()
                && meet.getReceiverInternalId() != null && !meet.getReceiverInternalId().isEmpty()
                && meet.getTask() != null
                && meet.getDate() != null
                && meet.getHour() != null
                && meet.getDuration() != null
                && meet.getInternalId() != null && !meet.getInternalId().isEmpty()
                && meet.getLastUser() != null;
    }

}
