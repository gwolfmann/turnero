package com.espou.turnero.processor;

import com.espou.turnero.model.Receiver;
import com.espou.turnero.service.ReceiverService;
import com.espou.turnero.storage.ReceiverDTO;
import com.espou.turnero.storage.ReceiverMapper;
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
public class ReceiverPipeline {
    private final Pipeline<ReceiverDTO, Receiver, Receiver> singleReadPipeline;
    private final Pipeline<List<ReceiverDTO>, List<Receiver>, List<Receiver>> listReadPipeline;
    private final Pipeline<ReceiverDTO, ReceiverDTO, ReceiverDTO> singleWritePipeline;

    private final Logger logger = LoggerFactory.getLogger(ReceiverPipeline.class);

    private final ReceiverService receiverService;

    @Autowired
    public ReceiverPipeline(ReceiverService receiverService) {
        this.receiverService = receiverService;
        singleReadPipeline = singleReadPipelineBuilder();
        listReadPipeline = listReadPipelineBuilder();
        singleWritePipeline = writePipelineBuilder();
    }

    public Mono<ServerResponse> getReceiverList(ServerRequest serverRequest) {
        logger.info("Received GET request for receiver list");
        return listReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getReceiverSingle(ServerRequest serverRequest) {
        return singleReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> writeSingleReceiver(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> deleteReceiver(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    private Mono<List<Receiver>> mapListToReceiver(List<ReceiverDTO> list) {
        return Mono.just(list.stream()
                .map(ReceiverMapper::toEntity)
                .collect(Collectors.toList()));
    }

    private Pipeline<List<ReceiverDTO>, List<Receiver>, List<Receiver>> listReadPipelineBuilder() {
        return Pipeline.<List<ReceiverDTO>, List<Receiver>, List<Receiver>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> receiverService.getAllReceivers().collectList())
                .boProcessor(this::mapListToReceiver)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<ReceiverDTO, Receiver, Receiver> singleReadPipelineBuilder() {
        return Pipeline.<ReceiverDTO, Receiver, Receiver>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleReceiver)
                .boProcessor(x -> Mono.just(ReceiverMapper.toEntity(x)))
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<ReceiverDTO, ReceiverDTO, ReceiverDTO> writePipelineBuilder() {
        return Pipeline.<ReceiverDTO, ReceiverDTO, ReceiverDTO>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(this::validateBody)
                .storageOp(this::writeReceiver)
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Mono<ReceiverDTO> getSingleReceiver(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("id")) {
            logger.info("Received GET request for receiver with Id: {}", vars.get("id"));
            return receiverService.getReceiverById(vars.get("id"));
        }
        if (vars.containsKey("internalId")) {
            logger.info("Received GET request for receiver with internalId: {}", vars.get("internalId"));
            return receiverService.getReceiverByInternalId(vars.get("internalId"));
        }
        return Mono.empty();
    }

    private Mono<ReceiverDTO> writeReceiver(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("internalId")) {
            String internalId = vars.get("internalId");
            if (serverRequest.method().equals(HttpMethod.DELETE)) {
                logger.info("Received DELETE request for receiver with internalId: {}", internalId);
                return receiverService.deleteReceiver(internalId);
            } else {
                logger.info("Received PUT request for receiver with internalId: {}", internalId);
                return serverRequest.bodyToMono(Receiver.class)
                        .flatMap(x -> Mono.just(ReceiverMapper.toDto(x)))
                        .flatMap(x -> receiverService.updateReceiver(x, internalId));
            }
        } else {
            logger.info("Received POST request for receiver");
            return serverRequest.bodyToMono(Receiver.class)
                    .flatMap(x -> Mono.just(ReceiverMapper.toDto(x)))
                    .flatMap(receiverService::writeReceiver);
        }
    }

    private Mono<ServerRequest> validateBody(ServerRequest serverRequest) {
        if (serverRequest.method().equals(HttpMethod.DELETE)) {
            return Mono.just(serverRequest);
        }
        return serverRequest.bodyToMono(ReceiverDTO.class)
                .flatMap(receiverDTO -> {
                    if (isReceiverValid(receiverDTO)) {
                        return Mono.just(ServerRequest.from(serverRequest).body(Pipeline.asJson(receiverDTO)).build());
                    } else {
                        return Mono.error(new RuntimeException("No proper body in the receiver Post " + receiverDTO));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Empty request body")));
    }

    private boolean isReceiverValid(ReceiverDTO receiver) {
        return receiver.getName() != null && !receiver.getName().isEmpty()
                && receiver.getAffiliation() != null && !receiver.getAffiliation().isEmpty()
                && receiver.getIdentification() != null
                && receiver.getInternalId() != null && !receiver.getInternalId().isEmpty();
    }
}

