package com.espou.turnero.processor;

import com.espou.turnero.model.Provider;
import com.espou.turnero.service.ProviderService;
import com.espou.turnero.storage.ProviderDTO;
import com.espou.turnero.storage.ProviderMapper;
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
public class ProviderPipeline {

    private final Pipeline<ProviderDTO, Provider, Provider> singleReadPipeline;
    private final Pipeline<List<ProviderDTO>, List<Provider>, List<Provider>> listReadPipeline;
    private final Pipeline<ProviderDTO, ProviderDTO, ProviderDTO> singleWritePipeline;

    private final Logger logger = LoggerFactory.getLogger(ProviderPipeline.class);

    private final ProviderService providerService;

    @Autowired
    public ProviderPipeline(ProviderService providerService) {
        this.providerService = providerService;
        singleReadPipeline = singleReadPipelineBuilder();
        listReadPipeline = listReadPipelineBuilder();
        singleWritePipeline = writePipelineBuilder();
    }

    public Mono<ServerResponse> getProviderList(ServerRequest serverRequest) {
        logger.info("Received GET request for provider list");
        return listReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> getProviderSingle(ServerRequest serverRequest) {
        return singleReadPipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> writeSingleProvider(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    public Mono<ServerResponse> deleteProvider(ServerRequest serverRequest) {
        return singleWritePipeline.executeToServerResponse(serverRequest);
    }

    private Mono<List<Provider>> mapListToProvider(List<ProviderDTO> list) {
        return Mono.just(list.stream()
                .map(ProviderMapper::toEntity)
                .collect(Collectors.toList()));
    }

    private Pipeline listReadPipelineBuilder() {
        return Pipeline.<List<ProviderDTO>, List<Provider>, List<Provider>>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(x -> providerService.getAllProviders().collectList())
                .boProcessor(this::mapListToProvider)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline singleReadPipelineBuilder() {
        return Pipeline.<ProviderDTO, Provider, Provider>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::getSingleProvider)
                .boProcessor(x -> Mono.just(ProviderMapper.toEntity(x)))
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Pipeline writePipelineBuilder() {
        return Pipeline.<ProviderDTO, ProviderDTO, ProviderDTO>builder()
                .validateRequest(Pipeline::noOp)
                .validateBody(Pipeline::noOp)
                .storageOp(this::writeProvider)
                .boProcessor(Pipeline::noOp)
                .presenter(Pipeline::noOp)
                .handleErrorResponse(Mono::error)
                .build();
    }

    private Mono<ProviderDTO> getSingleProvider(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("id")) {
            logger.info("Received GET request for provider with Id: {}", vars.get("id"));
            return providerService.getProviderById(vars.get("id"));
        }
        if (vars.containsKey("internalId")) {
            logger.info("Received GET request for provider with internalId: {}", vars.get("internalId"));
            return providerService.getProviderByInternalId(vars.get("internalId"));
        }
        return Mono.empty();
    }

    private Mono<ProviderDTO> writeProvider(ServerRequest serverRequest) {
        Map<String, String> vars = serverRequest.pathVariables();
        if (vars.containsKey("internalId")) {
            String internalId = vars.get("internalId");
            if (serverRequest.method().equals(HttpMethod.DELETE)) {
                logger.info("Received DELETE request for provider with internalId: {}", internalId);
                return serverRequest.bodyToMono(Provider.class)
                        .flatMap(x -> Mono.just(ProviderMapper.toDto(x)))
                        .flatMap(x -> providerService.deleteProvider(x, internalId));
            } else {
                logger.info("Received PUT request for provider with internalId: {}", internalId);
                return serverRequest.bodyToMono(Provider.class)
                        .flatMap(x -> Mono.just(ProviderMapper.toDto(x)))
                        .flatMap(x -> providerService.updateProvider(x, internalId));
            }
        } else {
            logger.info("Received POST request for provider");
            return serverRequest.bodyToMono(Provider.class)
                    .flatMap(x -> Mono.just(ProviderMapper.toDto(x)))
                    .flatMap(providerService::writeProvider);
        }
    }
}
