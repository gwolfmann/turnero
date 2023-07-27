package com.espou.turnero.processor;

import com.espou.turnero.model.Meet;
import com.espou.turnero.service.MeetService;
import com.espou.turnero.service.ProviderService;
import com.espou.turnero.service.ReceiverService;
import com.espou.turnero.service.ResourceService;
import com.espou.turnero.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class MeetPipeline {
    private final Pipeline<MeetDTO, Meet, Meet> singleReadPipeline;
    private final Pipeline<List<MeetDTOLookup>, List<Meet>, List<Meet>> listReadPipeline;
    private final Pipeline<List<MeetDTO>, List<MeetDTO>, List<MeetDTO>> listForaDatePipeline;
    private final Pipeline<MeetDTO, MeetDTO, MeetDTO> singleWritePipeline;

    private final Logger logger = LoggerFactory.getLogger(MeetPipeline.class);

    private final MeetService meetService;
    private final ResourceService resourceService;
    private final ProviderService providerService;
    private final ReceiverService receiverService;


    @Autowired
    public MeetPipeline(MeetService meetService,
                        ResourceService resourceService,
                        ProviderService providerService,
                        ReceiverService receiverService) {
        this.meetService = meetService;
        this.resourceService = resourceService;
        this.providerService = providerService;
        this.receiverService = receiverService;
        singleReadPipeline = singleReadPipelineBuilder();
        listReadPipeline = listReadPipelineBuilder();
        listForaDatePipeline = listForaDatePipelineBuilder();
        singleWritePipeline = writePipelineBuilder();
    }

    public Mono<ServerResponse> getMeetList(ServerRequest serverRequest) {
        logger.info("Received GET request for meet list");
        return listReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getMeetSingle(ServerRequest serverRequest) {
        return singleReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getMeetForDate(ServerRequest serverRequest) {
        return listForaDatePipeline.executeToServerResponse(serverRequest);
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

    private Mono<List<Meet>> mapListToMeet(List<MeetDTOLookup> list) {
        return Mono.just(list.stream()
                .map(MeetMapper::toEntity)
                .collect(Collectors.toList()));
    }

    private Pipeline<MeetDTO, Meet, Meet> singleReadPipelineBuilder() {
        return Pipeline.<MeetDTO, Meet, Meet>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleMeet)
                .boProcessor(this::getRelated)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<List<MeetDTOLookup>, List<Meet>, List<Meet>> listReadPipelineBuilder() {
        return Pipeline.<List<MeetDTOLookup>, List<Meet>, List<Meet>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> meetService.getAllMeets(x).collectList())
                .boProcessor(this::mapListToMeet)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline<List<MeetDTO>, List<MeetDTO>, List<MeetDTO>> listForaDatePipelineBuilder() {
        return Pipeline.<List<MeetDTO>, List<MeetDTO>, List<MeetDTO>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getListForDate)
                .boProcessor(Pipeline::noOp)
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
    private Mono<List<MeetDTO>> getListForDate(ServerRequest serverRequest) {
        MultiValueMap<String, String> allHeaders = serverRequest.queryParams();
        List<String> providers = allHeaders.get("provider");
        List<String> resources = allHeaders.get("resource");
        List<String> dates = allHeaders.get("date");
        String provider = null;
        String resource = null;
        LocalDate dateOfQuery = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (providers.size()>0) {
            provider = providers.get(0);
        } else {
            return Mono.error(new RuntimeException("No header provider"));
        }
        if (resources.size()>0) {
            resource = resources.get(0);
        } else {
            return Mono.error(new RuntimeException("No header resource"));
        }
        if (dates.size()>0) {
            dateOfQuery = LocalDate.parse(dates.get(0), dateTimeFormatter);
        } else {
            return Mono.error(new RuntimeException("No header date"));
        }
        logger.info("Received GET request for meet for resource {}, provider {} and date  {}", resource,provider,dateOfQuery.format(dateTimeFormatter));
        return meetService.getMeetsByResourceAndProviderAndDate(resource,provider,dateOfQuery);
    }

    private Mono<MeetDTO> writeMeet(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("internalId")) {
            String internalId = vars.get("internalId");
            if (serverRequest.method().equals(HttpMethod.DELETE)) {
                logger.info("Received DELETE request for meet with internalId: {}", internalId);
                return serverRequest.bodyToMono(MeetDTO.class)
                        .flatMap(x -> meetService.deleteMeet(internalId));
            } else {
                logger.info("Received PUT request for meet with internalId: {}", internalId);
                return serverRequest.bodyToMono(MeetDTO.class)
                        .flatMap(x -> meetService.updateMeet(x, internalId));
            }
        } else {
            logger.info("Received POST request for meet ");
            return serverRequest.bodyToMono(MeetDTO.class)
                    .flatMap(meetService::createMeet);
        }
    }
    private Mono<ServerRequest> validateBody(ServerRequest serverRequest) {
        if (serverRequest.method().equals(HttpMethod.DELETE)) {
            return Mono.just(serverRequest);
        }
        return serverRequest.bodyToMono(MeetDTO.class)
                .flatMap(meetDTO -> {
                    if (isMeetValid(meetDTO)) {
                        return Mono.just(ServerRequest.from(serverRequest).body(Pipeline.asJson(meetDTO)).build());
                    } else {
                        return Mono.error(new RuntimeException("No proper body in the meet Post "+meetDTO));
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

    private Mono<Meet> getRelated(MeetDTO meetDTO){
        Meet meet = MeetMapper.toEntity(meetDTO);
        return Mono.just(meet)
                .flatMap(m-> setResource(m, meetDTO.getResourceInternalId()))
                .flatMap(m-> setProvider(m, meetDTO.getProviderInternalId()))
                .flatMap(m-> setReceiver(m, meetDTO.getReceiverInternalId()));
    }

    private Mono<Meet> setResource(Meet meet,String resourceInternalId){
        return resourceService.getResourceByInternalId(resourceInternalId)
                .map(ResourceMapper::toEntity)
                .flatMap(resource -> {meet.setResource(resource);
                    return Mono.just(meet);})
                .switchIfEmpty(Mono.just(meet));
    }
    private Mono<Meet> setProvider(Meet meet,String providerInternalId){
        return providerService.getProviderByInternalId(providerInternalId)
                .map(ProviderMapper::toEntity)
                .flatMap(provider -> {meet.setProvider(provider);
                    return Mono.just(meet);})
                .switchIfEmpty(Mono.just(meet));
    }
    private Mono<Meet> setReceiver(Meet meet,String receiverInternalId){
        return receiverService.getReceiverByInternalId(receiverInternalId)
                .map(ReceiverMapper::toEntity)
                .flatMap(receiver -> {meet.setReceiver(receiver);
                    return Mono.just(meet);})
                .switchIfEmpty(Mono.just(meet));
    }
}
